package es.ucm.fdi.iw.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.services.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("products")
@AllArgsConstructor
public class DetailProduct {

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
    @ResponseBody
    public ResponseEntity<String> realizarPuja(@PathVariable long id, @RequestParam Double puja) {
        ProductDTO producto = productService.getProduct(id);

        
        if (puja.compareTo(producto.getPrecio()) > 0) {

            producto.setPrecio(puja); 
            productService.updateProduct(producto);  
            return ResponseEntity.ok("Puja realizada con éxito. Nuevo precio: €" + puja);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La puja debe ser mayor al precio actual.");
        }
    }
}
