package es.ucm.fdi.iw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import es.ucm.fdi.iw.business.dto.CreateProductDTO;
import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.dto.UserDTO;
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
        pujaDTO.setUsuarioId(userDTO.getId());
        pujaDTO.setSubastaId(id);
        pujaDTO.setDineroPujado(puja);
        pujaService.updatePuja(pujaDTO);
        userService.subtractMoney(userDTO.getId(), puja);

        if (puja.compareTo(producto.getPrecio()) > 0) {
            producto.setPrecioActual(puja);
            User usuario = (User) session.getAttribute("u");
            producto.setMaximoPujador(usuario.getUsername());
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
    public String nuevaSubasta(Model model, HttpSession session, @ModelAttribute("product") CreateProductDTO product) {
        User creador = (User) session.getAttribute("u");

        LocalDateTime fechaInicio = LocalDateTime.now();
        //LocalDateTime fechaFin = LocalDateTime.now().plusMinutes(1); 
        LocalDateTime fechaFin = LocalDateTime.now().plusSeconds(10);
        //LocalDateTime fechaInicio = LocalDateTime.parse(product.getFechaInicio(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //LocalDateTime fechaFin = LocalDateTime.parse(product.getFechaFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        // SubastaMapper.INSTANCE.createProductDTOToProductDTO(product);
        
        ProductDTO productDTO = new ProductDTO();
        productDTO.setFechaInicio(fechaInicio);
        productDTO.setFechaFin(fechaFin);
        productDTO.setPrecio(product.getPrecio());
        productDTO.setPrecioActual(product.getPrecio());
        productDTO.setNombre(product.getNombre());
        productDTO.setDescripcion(product.getDescripcion());
        productDTO.setCreadorUserId(creador.getId());
        productDTO.setEnabled(true);
        productService.createSubasta(productDTO);

        return "redirect:/index";
    }
}