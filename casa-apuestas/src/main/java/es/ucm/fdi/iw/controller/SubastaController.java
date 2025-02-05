package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("subastas")
public class SubastaController {

    @GetMapping("/")
    public String subastas() {
        return "subastas";
    }
    @GetMapping("/objeto")
    public String objeto() {
        return "objeto";
    }
    
}
