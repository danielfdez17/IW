package es.ucm.fdi.iw.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.business.dto.CreateProductDTO;
import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.enums.EstadoSubasta;
import es.ucm.fdi.iw.business.enums.RepartoSubasta;
import es.ucm.fdi.iw.business.fileconfiglocal.LocalData;
import es.ucm.fdi.iw.business.mapper.LocalDateTimeMapper;
import es.ucm.fdi.iw.business.model.Subasta;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.services.product.ProductService;
import es.ucm.fdi.iw.business.services.puja.PujaService;
import es.ucm.fdi.iw.business.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//markdown
import org.commonmark.parser.Parser;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;


@Slf4j
@Controller
@RequestMapping("products")
@AllArgsConstructor
public class DetailProductController {

    private final ProductService productService;
    private final PujaService pujaService;
    private final UserService userService;
    private final LocalData localData;
    private final SimpMessagingTemplate messagingTemplate;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("productos", productService.getAllProducts());
        return "subastas";
    }

    
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        ProductDTO producto = productService.getProduct(id);

        // Convertir la descripción de Markdown a HTML
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        Node document = parser.parse(producto.getDescripcion());
        String descripcionMarkdown = renderer.render(document);
        producto.setDescripcion(descripcionMarkdown);  


        model.addAttribute("producto", producto);
        model.addAttribute("estado", EstadoSubasta.getTxt(producto.getEstadoSubasta()));

        User session = (User) model.getAttribute("u");
        Long winner = -1L;
        if (producto.getIdUserGanador() != null && EstadoSubasta.FINALIZADA.equals(producto.getEstadoSubasta()))
            winner = producto.getIdUserGanador();

        model.addAttribute("canAddComment",
                winner == session.getId() && RepartoSubasta.ENTREGADO.equals(producto.getRepartoSubasta()));
        model.addAttribute("isGanador", winner == session.getId());
        model.addAttribute("pictureNames", getPictureNames(id));

        return "productdetail";
    }
 
/* 
 *     @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        ProductDTO producto = productService.getProduct(id);
        model.addAttribute("producto", producto);
        model.addAttribute("estado", EstadoSubasta.getTxt(producto.getEstadoSubasta()));
        User session = (User) model.getAttribute("u");
        Long winner = -1L;
        if (producto.getIdUserGanador() != null && EstadoSubasta.FINALIZADA.equals(producto.getEstadoSubasta()))
            winner = producto.getIdUserGanador();

        model.addAttribute("canAddComment",
                winner == session.getId() && RepartoSubasta.ENTREGADO.equals(producto.getRepartoSubasta()));
        model.addAttribute("isGanador", winner == session.getId());
        model.addAttribute("pictureNames", getPictureNames(id));
        return "productdetail";
    }
*/


    @PostMapping("/{id}/pujar")
    @ResponseBody
    public Map<String, String> realizarPuja(@PathVariable long id, @RequestParam Double puja, HttpSession session) {
        ProductDTO producto = productService.getProduct(id);
        UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = userService.findUserByUsername(u.getUsername());
        Subasta subasta = productService.getSubasta(id);
        PujaDTO pujaDTO = new PujaDTO();

        if (puja.compareTo(producto.getPrecioActual()) > 0 || subasta.getPujas().isEmpty()) {
            pujaDTO.setUsuarioId(userDTO.getId());
            pujaDTO.setSubastaId(id);
            pujaDTO.setDineroPujado(puja);
            pujaService.updatePuja(pujaDTO);

            User usuario = (User) session.getAttribute("u");
            producto.setDineroPujado(id);
            producto.setPrecioActual(puja);
            producto.setMaximoPujador(usuario.getUsername());
            producto.setUsuarioHaPujado(true);
            productService.updateProduct(producto);

            sendProductUpdateToWebSocket(producto);
        }
        Double x = ((User) session.getAttribute("u")).getAvailableMoney();
        return Map.of(
                "username", u.getUsername(),
                "availableMoney", String.valueOf(x));
    }

    private void sendProductUpdateToWebSocket(ProductDTO producto) {
        messagingTemplate.convertAndSend("/topic/product-updates/" + producto.getId(), producto);
    }

    @PostMapping("/send_product/{id}")
    public String updateSendProduct(@PathVariable long id, @RequestParam String reparto, HttpSession session) {
        productService.updateSendProduct(id, reparto);
        User usuario = (User) session.getAttribute("u");
        return "redirect:/user/" + usuario.getId();
    }

    @PostMapping("/nueva_subasta")
    @Transactional
    public String nuevaSubasta(Model model, HttpSession session,
            @ModelAttribute("product") CreateProductDTO product,
            @RequestParam(required = true) LocalDate nuevaFechaInicio,
            @RequestParam(required = true) LocalDate nuevaFechaFin,
            @RequestParam(required = true) LocalTime nuevaHoraInicio,
            @RequestParam(required = true) LocalTime nuevaHoraFin,
            @RequestParam(required = false) MultipartFile photos[]) throws Exception {
        User creador = (User) session.getAttribute("u");
        LocalDateTime newDateInit = LocalDateTimeMapper.toLocalDateTime(nuevaFechaInicio, nuevaHoraInicio);
        LocalDateTime newDateEnd = LocalDateTimeMapper.toLocalDateTime(nuevaFechaFin, nuevaHoraFin);
        if (newDateInit.isAfter(newDateEnd)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        LocalDateTime now = LocalDateTime.now();
        EstadoSubasta estadoSubasta = EstadoSubasta.PENDIENTE;
        if (now.isAfter(newDateInit) || now.isEqual(newDateInit))
            estadoSubasta = EstadoSubasta.EN_CURSO;

        ProductDTO productDTO = new ProductDTO();
        productDTO.setFechaInicio(newDateInit);
        productDTO.setFechaFin(newDateEnd);
        productDTO.setPrecio(product.getPrecio());
        productDTO.setPrecioActual(product.getPrecio());
        productDTO.setNombre(product.getNombre());
        productDTO.setDescripcion(product.getDescripcion());
        productDTO.setCreadorUserId(creador.getId());
        productDTO.setEnabled(true);
        productDTO.setUsuarioHaPujado(false);
        productDTO.setEstadoSubasta(estadoSubasta);
        ProductDTO subasta = productService.createSubasta(productDTO);
        updatePicture(photos, subasta.getId());

        return "redirect:/index";
    }

    @PostMapping("/{id}")
    @Transactional
    public String editarSubasta(
            @PathVariable long id,
            @ModelAttribute("product") ProductDTO product,
            @RequestParam(required = false) MultipartFile photos[],
            @RequestParam(required = true) LocalDate nuevaFechaFin,
            Model model, HttpSession session) throws Exception {
        product.setFechaFin(LocalDateTimeMapper.toLocalDateTime(nuevaFechaFin, LocalTime.now()));
        productService.updateProduct(product);
        updatePicture(photos, id);

        return "redirect:/index";
    }

    /**
     * Downloads a profile pic for a user id
     * 
     * @param id
     * @return
     * @throws IOException
     */
    @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "iwdata", "subastas", "" + id);
        File folder = path.toFile();
        File f = null;
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(file -> file.isFile());
            if (files != null && files.length > 0) {
                f = files[0];
            }
        }
        if (f == null) {
            f = localData.getFile("subastas", "" + id + ".jpg");
        }
        InputStream in = new BufferedInputStream(
                f.exists() ? new FileInputStream(f) : DetailProductController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }

    @GetMapping("{id}/{picName}")
    public StreamingResponseBody getPic(@PathVariable long id, @PathVariable String picName) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "iwdata", "subastas", "" + id);
        File folder = path.toFile();
        File f = null;
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(file -> file.isFile() && file.getName().equals(picName));
            if (files != null && files.length > 0) {
                f = files[0];
            }
        }
        if (f == null) {
            f = localData.getFile("subastas", "" + id + ".jpg");
        }
        InputStream in = new BufferedInputStream(
                f.exists() ? new FileInputStream(f) : DetailProductController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }

    @PostMapping("{id}/comentar")
    public String comentarProducto(@PathVariable Long id,
            @RequestParam String comentario,
            @RequestParam byte valoracion) {
        productService.addComentarioYValoracion(id, comentario, valoracion);
        return "redirect:/products/" + id;
    }

    /**
     * Returns the default profile pic
     * 
     * @return
     */
    private static InputStream defaultPic() {
        return new BufferedInputStream(Objects.requireNonNull(
                UserController.class.getClassLoader().getResourceAsStream(
                        "static/img/BARATO.png")));
    }

    private void updatePicture(MultipartFile photos[], long id) {
        for (MultipartFile photo : photos) {
            if (!photo.getOriginalFilename().isBlank()) {
                try {
                    Path path = Paths.get(System.getProperty("user.dir"), "iwdata", "subastas", "" + id,
                            photo.getOriginalFilename() + ".jpg");
                    File file = path.toFile();

                    file.getParentFile().mkdirs();

                    try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file))) {
                        byte[] bytes = photo.getBytes();
                        stream.write(bytes);
                        log.info("Uploaded photo for {} into {}!", id, file.getAbsolutePath());
                    } catch (Exception e) {
                        log.warn("Error uploading " + id + " ", e);
                        if (file.createNewFile()) {
                            log.info("Uploaded photo for {} into {}!", id, file.getAbsolutePath());
                        } else {
                            log.warn("Error uploading " + id + " ", e);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> getPictureNames(Long id) {
        Path path = Paths.get(System.getProperty("user.dir"), "iwdata", "subastas", "" + id);
        File folder = path.toFile();
        List<String> fileNames = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(file -> file.isFile());
            if (files != null && files.length > 0) {
                for (File f : files) {
                    fileNames.add(f.getName());
                }
            }
        }
        return fileNames;
    }

}