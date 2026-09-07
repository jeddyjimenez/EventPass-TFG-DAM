package com.tfg.eventos.controlador;

import com.tfg.eventos.servicio.UsuarioService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.entidad.enums.EstadoEvento;
import com.tfg.eventos.entidad.enums.EstadoPago;
import com.tfg.eventos.entidad.enums.TipoEvento;
import com.tfg.eventos.servicio.AsistenteService;
import com.tfg.eventos.servicio.EntradaService;
import com.tfg.eventos.servicio.EventoService;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class EventoController {
     private final UsuarioService usuarioService;
     private final EventoService eventoService;
     private final AsistenteService asistenteService;
     private final EntradaService entradaService;
    public EventoController(EventoService eventoService, UsuarioService usuarioService, AsistenteService asistenteService, EntradaService entradaService){
        this.eventoService = eventoService;
        this.usuarioService = usuarioService; 
        this.asistenteService = asistenteService;
        this.entradaService = entradaService;
    }

    @GetMapping("/eventos")
    public String listarEventos(Model model){
        List<Evento> todos = eventoService.obtenerTodos();
        List<Evento> publicados = new ArrayList<>();
        // Solo se muestran en la web pública los eventos publicados
        for (Evento evento : todos){
            if (evento.getEstado() == EstadoEvento.PUBLICADO){
                publicados.add(evento);
            }
        }
        model.addAttribute("eventos", publicados);
        return "eventos_lista";
    }

    @GetMapping("/busqueda")
    public String buscarEventos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) TipoEvento tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String ciudad,
            Model model) {

        // Solo se considera búsqueda real si se ha rellenado algún filtro
        boolean busquedaRealizada = (nombre != null && !nombre.isBlank())
                || tipo != null
                || fechaDesde != null
                || fechaHasta != null
                || (ciudad != null && !ciudad.isBlank());

        List<Evento> resultados = busquedaRealizada
                ? eventoService.buscar(nombre, tipo, fechaDesde, fechaHasta, ciudad)
                : new ArrayList<>();

        model.addAttribute("resultados", resultados);
        model.addAttribute("busquedaRealizada", busquedaRealizada);
        model.addAttribute("tiposEvento", TipoEvento.values());
        model.addAttribute("nombre", nombre);
        model.addAttribute("tipo", tipo);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);
        model.addAttribute("ciudad", ciudad);
        return "busqueda";
    }
    @GetMapping("/eventos/{id}")
    public String mostrarEvento(@PathVariable Long id, Model model){
        Optional<Evento> evento = eventoService.obtenerPorId(id);
        if (evento.isEmpty()){
            return "noexiste";
        }
        model.addAttribute("evento", evento.get());
        return "evento_detalle";
    }
    @PostMapping("/eventos/{id}/reservar")
    public String reservarEvento(@PathVariable Long id, Authentication authentication) {
        Optional<Evento> evento = eventoService.obtenerPorId(id);
        if (evento.isEmpty()){
            return "noexiste";
        }
        String usuariologueado = authentication.getName();
        Optional<Usuario> usuarioExiste = usuarioService.obtenerPorEmail(usuariologueado);
        if (usuarioExiste.isEmpty()){
            return "noexiste";
        }
        Evento eventoReal = evento.get();
        // No se puede reservar un evento que aún no esté publicado
        if (eventoReal.getEstado() != EstadoEvento.PUBLICADO){
            return "redirect:/eventos/" + id + "?error=no-publicado";
        }
        Usuario usuarioReal = usuarioExiste.get();
        // Evita que un usuario reserve dos veces la misma entrada activa
        if (entradaService.existeEntradaActivaPorUsuarioYEvento(usuarioReal, eventoReal)) {
            return "redirect:/eventos/" + id + "?error=yareservado";
        }

        // Se comprueba si el aforo del evento ya está completo
        int asistentesActuales = asistenteService.obtenerPorEvento(eventoReal).size();
        if (asistentesActuales >= eventoReal.getCapacidad()){
            return "redirect:/eventos/" + id + "?error=aforo-completo";
        }

        // Si el usuario aún no está asociado a ese evento, se crea el asistente
        Optional<Asistente> asistenteExistente = asistenteService.obtenerPorUsuarioYEvento(usuarioReal, eventoReal);
        Asistente asistenteReserva = asistenteExistente.orElseGet(() -> asistenteService.guardar(new Asistente(usuarioReal, eventoReal)));

        // Cada entrada se crea con un token QR único
        String qrToken = UUID.randomUUID().toString();
        Entrada entradaNueva = new Entrada(qrToken, EstadoEntrada.ACTIVA, EstadoPago.PENDIENTE, LocalDateTime.now(), asistenteReserva);
        entradaService.guardar(entradaNueva);
        return "redirect:/eventos/" + id + "?ok=reservada";
    }
    
}
