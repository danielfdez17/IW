package es.ucm.fdi.iw.config;


import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.ucm.fdi.iw.business.model.User;
import es.ucm.fdi.iw.business.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class GlobalModel {
    private final UserRepository userRepository;
    private final HttpSession session;

    @ModelAttribute("saldo")
    public double saldoActual(@AuthenticationPrincipal UserDetails user) {
        if (user == null)
            return 0;
        Optional<User> u = userRepository.findByUsername(user.getUsername());
        if (u.isEmpty())
            return 0;
        session.setAttribute("u", u.get());
        return u.get().getAvailableMoney();
    }
}
