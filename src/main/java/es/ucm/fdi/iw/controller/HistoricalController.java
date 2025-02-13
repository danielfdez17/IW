package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("historical")
@Controller
public class HistoricalController {

    @GetMapping("/")
    public String historical() {
        return "historical";
    }

}
