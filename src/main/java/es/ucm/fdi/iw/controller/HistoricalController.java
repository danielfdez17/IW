package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.business.services.historical.HistoricalServices;
import lombok.AllArgsConstructor;

@RequestMapping("historical")
@Controller
@AllArgsConstructor
public class HistoricalController {

    private final HistoricalServices historicalService;

    @GetMapping("/")
    public String historical(Model model) {
        model.addAttribute("historicalBids", historicalService.getHistorical());
        return "historical";
    }

}
