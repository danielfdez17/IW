package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.services.product.ProductService;
import es.ucm.fdi.iw.business.services.user.UserService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

	
    @Autowired
    private EntityManager entityManager;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {        
        for (String name : new String[] {"u", "url", "ws"}) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    private static final Logger log = LogManager.getLogger(AdminController.class);

	@GetMapping("/")
    public String index(Model model) {
        log.info("Admin acaba de entrar");
        model.addAttribute("users", 
            entityManager.createQuery("select u from User u").getResultList());
        return "admin";
    }

    @GetMapping("/subasta")
    public String adminSubasta(Model model) {
        log.info("Admin acaba de entrar");
        model.addAttribute("products", productService.getAllProducts());
        return "admin-subasta";
    }

    @PostMapping("/user/disable/{id}")
    public String disableUser(@PathVariable long id) {
        log.info("Admin deshabilita " + id);
        userService.disableUser(id);
        return "redirect:/admin/";
    }

    @PostMapping("/user/enable/{id}")
    public String enableUser(@PathVariable long id) {
        log.info("Admin deshabilita " + id);
        userService.enableUser(id);
        return "redirect:/admin/";
    }

    @PostMapping("/subasta/disabled/{id}")
    public String disabledSubasta(@PathVariable long id) {
        productService.disabledProduct(id);
        return "redirect:/admin/subasta";
    }

    @PostMapping("/subasta/update")
    public String updateSubasta(ProductDTO product) {
        productService.updateAdminProduct(product);
        return "redirect:/admin/subasta";
    }
}