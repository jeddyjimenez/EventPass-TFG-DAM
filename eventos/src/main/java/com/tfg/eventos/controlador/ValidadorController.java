package com.tfg.eventos.controlador;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.RegistroAcceso;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.servicio.EntradaService;
import com.tfg.eventos.servicio.RegistroAccesoService;

@Controller
public class ValidadorController {
    private final EntradaService entradaService;
    private final RegistroAccesoService registroAccesoService;
    public ValidadorController(EntradaService entradaService, RegistroAccesoService registroAccesoService) {
        this.entradaService = entradaService;
        this.registroAccesoService = registroAccesoService;
    }
    @PostMapping("/validador/validar")
    public String validarEntrada(@RequestParam String qrToken) {
        // Busca la entrada a partir del token QR leído
        Optional<Entrada> entrada = entradaService.obtenerPorQrToken(qrToken);
        if (entrada.isEmpty()){
            return "redirect:/validador?error=noexiste";
        }
        Entrada entradaObtenida = entrada.get();
        if (entradaObtenida.getEstado() == EstadoEntrada.USADA){
            return "redirect:/validador?error=yausada";
        }
        if (entradaObtenida.getEstado() == EstadoEntrada.CANCELADA){
            return "redirect:/validador?error=cancelada";
        }
        // Si todo es correcto, la entrada pasa a estado USADA
        entradaObtenida.setEstado(EstadoEntrada.USADA);
        entradaObtenida.setUsadaEn(LocalDateTime.now());
        entradaService.guardar(entradaObtenida);
        // También se guarda un registro del acceso realizado
        RegistroAcceso registro = new RegistroAcceso();
        registro.setEntrada(entradaObtenida);
        registro.setFechaAcceso(LocalDateTime.now());
        registro.setObservaciones("Acceso validado.");
        registroAccesoService.guardar(registro);
        return "redirect:/validador?ok=valida";
    }
}

