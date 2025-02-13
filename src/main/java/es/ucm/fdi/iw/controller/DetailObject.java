package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("objetos")
public class DetailObject {
    
    @GetMapping
    public String objeto() {
        return "objeto";
    }

}
