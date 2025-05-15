package es.ucm.fdi.iw.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.dto.UserDTO;
import es.ucm.fdi.iw.business.services.product.ProductService;
import es.ucm.fdi.iw.business.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

/**
 * Non-authenticated requests only.
 */
@Controller
@AllArgsConstructor
public class RootController {

    private final ProductService productService;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

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

    @GetMapping("/signup")
    public String signup(Model model, HttpServletRequest request) {
        boolean error = request.getQueryString() != null && request.getQueryString().indexOf("error") != -1;
        model.addAttribute("signupError", error);
        return "signup";
    }

    @PostMapping("/signup")
    public String postSignup(Model model, HttpServletRequest request,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("delivery_address") String deliveryAddress,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        UserDTO userDTO = UserDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .deliveryAddress(deliveryAddress)
                .password(passwordEncoder.encode(password))
                .build();
        try {
            this.userService.createUser(userDTO);
        } catch (Exception e) {
            model.addAttribute("existingUsername", e.getMessage());
            return "signup";
        }
        boolean error = request.getQueryString() != null && request.getQueryString().indexOf("error") != -1;
        model.addAttribute("signupError", error);
        return "redirect:/login";
    }

    @GetMapping({ "/", "/index" })
    public String index(Model model) {
        List<ProductDTO> productos = productService.getAllProductsWithSaleActive();
        model.addAttribute("productos", productos);
        return "index";
    }
}
