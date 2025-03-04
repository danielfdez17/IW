package es.ucm.fdi.iw.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.services.product.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

/**
 * Non-authenticated requests only.
 */
@Controller
@AllArgsConstructor
public class RootController {

    private final ProductService productService;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        boolean error = request.getQueryString() != null && request.getQueryString().indexOf("error") != -1;
        model.addAttribute("loginError", error);
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<ProductDTO> productos = productService.getAllProducts();
        System.out.println("HOLA" + productos.size());

        model.addAttribute("productos", productos);
        
        return "index";
    }
}
