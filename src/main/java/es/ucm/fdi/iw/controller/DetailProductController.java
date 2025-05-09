package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.business.dto.CreateProductDTO;
import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.fileconfiglocal.LocalData;
import es.ucm.fdi.iw.business.mapper.LocalDateTimeMapper;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.services.product.ProductService;
import es.ucm.fdi.iw.business.services.puja.PujaService;
import es.ucm.fdi.iw.business.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("products")
@AllArgsConstructor
public class DetailProductController {

    private final ProductService productService;
    private final PujaService pujaService;
    private final UserService userService;
    private final LocalData localData;

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
    public String product(@PathVariable int id, Model model) {
        ProductDTO producto = productService.getProduct(id);
        model.addAttribute("producto", producto);
        return "productdetail";
    }

    @PostMapping("/{id}/pujar")
    public String realizarPuja(@PathVariable long id, @RequestParam Double puja, HttpSession session) {
        ProductDTO producto = productService.getProduct(id);
        UserDetails u = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = userService.findUserByUsername(u.getUsername());

        PujaDTO pujaDTO = new PujaDTO();

        if (puja.compareTo(producto.getPrecioActual()) > 0) {
            pujaDTO.setUsuarioId(userDTO.getId());
            pujaDTO.setSubastaId(id);
            pujaDTO.setDineroPujado(puja);
            pujaService.updatePuja(pujaDTO);
            userService.subtractMoney(userDTO.getId(), puja);

            User usuario = (User) session.getAttribute("u");
            producto.setDineroPujado(id);
            producto.setPrecioActual(puja);
            producto.setMaximoPujador(usuario.getUsername());
            producto.setUsuarioHaPujado(true);
            productService.updateProduct(producto);
            
            sendProductUpdateToWebSocket(producto); 
        }
        return "redirect:/products/" + id;
    }
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private void sendProductUpdateToWebSocket(ProductDTO producto) {
        messagingTemplate.convertAndSend("/topic/product-updates/" + producto.getId(), producto);
    }

    @PostMapping("/nueva_subasta")
    @Transactional
    public String nuevaSubasta(Model model, HttpSession session,
            @ModelAttribute("product") CreateProductDTO product,
            @RequestParam(required = true) LocalDate nuevaFechaInicio,
            @RequestParam(required = true) LocalDate nuevaFechaFin,
            @RequestParam(required = false) MultipartFile photo) throws Exception {
        User creador = (User) session.getAttribute("u");

        LocalDateTime fechaInicio = LocalDateTime.now();
        //LocalDateTime fechaFin = LocalDateTime.now().plusMinutes(1); 
        LocalDateTime fechaFin = LocalDateTime.now().plusSeconds(10);
        //LocalDateTime fechaInicio = LocalDateTime.parse(product.getFechaInicio(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //LocalDateTime fechaFin = LocalDateTime.parse(product.getFechaFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
       
        ProductDTO productDTO = new ProductDTO();
        productDTO.setFechaInicio(LocalDateTimeMapper.toLocalDateTime(nuevaFechaInicio));
        productDTO.setFechaFin(LocalDateTimeMapper.toLocalDateTime(nuevaFechaFin));
        productDTO.setPrecio(product.getPrecio());
        productDTO.setPrecioActual(product.getPrecio());
        productDTO.setNombre(product.getNombre());
        productDTO.setDescripcion(product.getDescripcion());
        productDTO.setCreadorUserId(creador.getId());
        productDTO.setEnabled(true);
        productDTO.setUsuarioHaPujado(false);
        ProductDTO subasta = productService.createSubasta(productDTO);

        updatePicture(photo, subasta.getId());

        return "redirect:/index";
    }

    @PostMapping("/{id}")
    @Transactional
    public String editarSubasta(
            @PathVariable long id,
            @ModelAttribute("product") ProductDTO product,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = true) LocalDate nuevaFechaFin,
            Model model, HttpSession session) throws Exception {
        product.setFechaFin(LocalDateTimeMapper.toLocalDateTime(nuevaFechaFin));
        productService.updateProduct(product);
        updatePicture(photo, id);

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
        File f = localData.getFile("subastas", "" + id + ".jpg");
        InputStream in = new BufferedInputStream(
                f.exists() ? new FileInputStream(f) : DetailProductController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
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

    private void updatePicture(MultipartFile photo, long id) {
        if (!photo.getOriginalFilename().isBlank()) {

            try {
                String filePath = System.getProperty("user.dir") + "\\iwdata\\subastas\\" + id + ".jpg";
                FileOutputStream fout = new FileOutputStream(filePath);
                fout.write(photo.getBytes());

                fout.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}