package es.ucm.fdi.iw.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import es.ucm.fdi.iw.business.dto.ComplainDTO;
import es.ucm.fdi.iw.business.services.coplains.ComplainService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@AllArgsConstructor
@RequestMapping("quejas")
public class ComplainController {

    private final ComplainService complainService;

    @PostMapping("/")
    public String addComplain(
            @RequestParam(required = true) Long userId,
            @RequestParam(required = true) String text) {

        ComplainDTO complainDTO = ComplainDTO.builder()
                .creatorId(userId)
                .text(text)
                .dateTime(LocalDateTime.now())
                .build();

        try {
            complainService.createComplain(complainDTO);
        } catch (Exception e) {
            log.error("Error al poner la queja", e);
        }

        return "redirect:/";
    }

    @GetMapping("/")
    public String getComplains(
            Model model, HttpSession request) {

        model.addAttribute("complains", complainService.getAllComplains());

        return "complains";
    }

}
