package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.business.dto.ProductDTO;

import es.ucm.fdi.iw.business.services.historical.HistoricalServices;
import es.ucm.fdi.iw.business.services.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RequestMapping("historical")
@Controller
@AllArgsConstructor
public class HistoricalController {

    private final HistoricalServices historicalService;
    private final ProductService productService;
    
    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }


    @GetMapping("/")
    public String historical(Model model) {
        model.addAttribute("historicalBids", historicalService.getHistorical());
        return "historical";
    }

    @GetMapping("/reviews/{id}")
    public String reviews(@PathVariable int id, Model model) {
        ProductDTO producto = productService.getProduct(id);
        
        if (producto == null) {
            return "redirect:/historical";  
        }

        model.addAttribute("producto", producto);
        return "reviews";
    }




}
