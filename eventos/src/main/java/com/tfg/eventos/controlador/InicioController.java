package com.tfg.eventos.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/")
    public String inicio(Authentication authentication) {
        // Si no hay sesión, se redirige a la lista pública de eventos
        if (authentication == null) {
            return "redirect:/eventos";
        }

        // Si es admin, se le manda directamente al panel de gestión
        if (authentication.getAuthorities().toString().contains("ROLE_ADMIN")) {
            return "redirect:/admin/eventos";
        }

        return "redirect:/eventos";
    }
}
