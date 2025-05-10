package es.ucm.fdi.iw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.dto.PujaDTO;
import es.ucm.fdi.iw.business.dto.ProductDTO;
import es.ucm.fdi.iw.business.services.archived.ArchivedServices;
import es.ucm.fdi.iw.business.services.product.ProductService;
import es.ucm.fdi.iw.business.services.puja.PujaService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@RequestMapping("archived")
@Controller
@AllArgsConstructor
public class ArchivedController {

    private final ArchivedServices archivedService;
    private final ProductService productService;
    private final PujaService pujaService; 
    
    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    @GetMapping("/")
    public String archived(Model model) {
        model.addAttribute("archivedBids", archivedService.getArchived());
        return "archived";
    }

    @GetMapping("/reviews/{id}")
    public String reviews(@PathVariable int id, HttpSession session, Model model) { 
        ProductDTO producto = productService.getProduct(id);
        User usuario = (User) session.getAttribute("u");
        Long userId = usuario.getId();
        
        if (producto == null) {
            return "redirect:/archived";  
        }

        // Obtener la puja asociada al usuario y subasta
        PujaDTO pujaDTO = pujaService.getPuja(userId, id);  // Necesitas obtener el userId adecuado
        
        // Si no hay puja asociada, usar valores predeterminados
        boolean isValorated = pujaDTO != null;
        int rating = isValorated ? pujaDTO.getPuntuacion() : 0;  // Obtener puntuación
        String comentario = isValorated ? pujaDTO.getComentario() : "";  // Obtener comentario

        // Agregar atributos al modelo
        model.addAttribute("producto", producto);
        model.addAttribute("isValorated", isValorated);
        model.addAttribute("rating", rating);
        model.addAttribute("comentario", comentario);

        return "reviews";
    }

    @PostMapping("/reviews/{id}")
    public String submitReview(@PathVariable int id, 
                            @RequestParam("rating") int rating, 
                            @RequestParam("comment") String comment, 
                            HttpSession session, 
                            Model model) {

        // Obtener el usuario de la sesión (asegurarse de que el usuario esté logueado)
        User usuario = (User) session.getAttribute("u");
        Long userId = usuario.getId();
        ProductDTO producto = productService.getProduct(id);
        
        if (producto == null || userId == null) {
            return "redirect:/archived";  
        }

        // Crear el DTO para la puja/reseña
        PujaDTO pujaDTO = new PujaDTO(userId, id, 0, rating, comment, java.time.LocalDateTime.now());

        // Guardar la reseña en la base de datos
        pujaService.savePuja(pujaDTO);

        return "redirect:/archived/reviews/" + id;
    }
}