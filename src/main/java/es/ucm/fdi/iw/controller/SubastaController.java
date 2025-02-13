package es.ucm.fdi.iw.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.services.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("subastas")
@AllArgsConstructor
public class SubastaController {
    
    private final ProductService productService;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    @GetMapping("/")
    public String subastas(Model model, HttpSession session) {
        List<ProductDTO> objetos = this.productService.getProducts();
        model.addAttribute("productos", objetos);
        return "subastas";
    }
}
