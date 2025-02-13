package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.services.product.ProductService;
import lombok.AllArgsConstructor;


@Controller
@RequestMapping("products")
@AllArgsConstructor
public class DetailProduct {

    private final ProductService productService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("productos", productService.getProducts());
        return "subastas";
    }

    @GetMapping("/{id}")
    public String product(@PathVariable int id, Model model) {
        ProductDTO producto = productService.getProduct(id);
        model.addAttribute("producto", producto);
        return "productdetail";
    }

}
