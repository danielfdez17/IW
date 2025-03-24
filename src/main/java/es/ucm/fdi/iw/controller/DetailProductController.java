package es.ucm.fdi.iw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.business.dto.CreateProductDTO;
import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.mapper.SubastaMapper;
import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.services.product.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("products")
@AllArgsConstructor
public class DetailProductController {

    private final ProductService productService;

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

        if (puja.compareTo(producto.getPrecio()) > 0) {
            producto.setPrecio(puja); 
            User usuario = (User) session.getAttribute("u");
            producto.setMaximoPujador(usuario.getUsername());

            productService.updateProduct(producto);  
            // return ResponseEntity.ok("Puja realizada con éxito. Nuevo precio: €" + puja);
        } 
        return "redirect:/products/" + id;
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La puja debe ser mayor al precio actual.");
    }

    @PostMapping("/nueva_subasta")
    @Transactional
    public String nuevaSubasta(Model model, HttpSession session, @ModelAttribute("product") CreateProductDTO product) {
        User creador = (User) session.getAttribute("u");

        LocalDateTime fechaInicio = LocalDateTime.now();
        //LocalDateTime fechaFin = LocalDateTime.now().plusMinutes(1); 
        LocalDateTime fechaFin = LocalDateTime.now().plusSeconds(20);
        //LocalDateTime fechaFin = LocalDateTime.parse(product.getFechaFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setEnabled(true);
        productDTO.setFechaInicio(fechaInicio);
        productDTO.setFechaFin(fechaFin);
        productDTO.setPrecio(product.getPrecio());
        productDTO.setNombre(product.getNombre());
        productDTO.setDescripcion(product.getDescripcion());
        productDTO.setCreadorUserId(creador.getId());

        productService.createSubasta(productDTO);

        return "redirect:/index";
    }


    @PostMapping("/{id}/toggle-puja")
    public String togglePuja(@PathVariable Long id) {
        ProductDTO producto = productService.getProduct(id);
        boolean currentEnabled = producto.isEnabled();
        producto.setEnabled(!currentEnabled); 
        productService.updateProduct(producto);
        return "redirect:/products/" + id;
    }

    @GetMapping("/products/status/{id}")
    @ResponseBody
    public Map<String, Boolean> getSubastaStatus(@PathVariable long id) {
        boolean isActive = productService.isProductActive(id);
        return Map.of("isActive", isActive);  // Retorna un JSON con la clave 'isActive'
    }
}