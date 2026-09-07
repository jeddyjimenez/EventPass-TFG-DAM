package com.tfg.eventos.controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.entidad.enums.EstadoEvento;
import com.tfg.eventos.servicio.AsistenteService;
import com.tfg.eventos.servicio.EntradaService;
import com.tfg.eventos.servicio.EventoService;
import com.tfg.eventos.servicio.SupabaseStorageService;
import com.tfg.eventos.servicio.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminEventoController{

    private final EventoService eventoService;
    private final UsuarioService usuarioService;
    private final AsistenteService asistenteService;
    private final EntradaService entradaService;
    private final SupabaseStorageService supabaseStorageService;

    public AdminEventoController(EventoService eventoService,
                                 UsuarioService usuarioService,
                                 AsistenteService asistenteService,
                                 EntradaService entradaService,
                                 SupabaseStorageService supabaseStorageService){
        this.eventoService = eventoService;
        this.usuarioService = usuarioService;
        this.asistenteService = asistenteService;
        this.entradaService = entradaService;
        this.supabaseStorageService = supabaseStorageService;
    }
    @GetMapping("/admin/eventos")
    public String eventosAdmin(Model model, Authentication authentication) {
        Optional<Usuario> adminLogueado = usuarioService.obtenerPorEmail(authentication.getName());
        if (adminLogueado.isEmpty()){
            return "noexiste";
        }
        // Muestra solo los eventos creados por el admin logueado
        List<Evento> eventosAdmin = eventoService.obtenerPorOrganizador(adminLogueado.get());
        model.addAttribute("eventos", eventosAdmin);
        return "admin/eventos";
    }
    @GetMapping("/admin/eventos/nuevo")
    public String nuevoEvento(Model model){
        Evento evento = new Evento();
        model.addAttribute("evento", evento);
        // Se cargan los validadores disponibles para poder asignarlos al evento
        model.addAttribute("validadoresDisponibles", usuarioService.obtenerValidadores());
        model.addAttribute("validadoresSeleccionadosIds", new ArrayList<Long>());
        return "admin/evento_form";
    }
    @PostMapping("/admin/eventos")
    public String postEvento(@ModelAttribute Evento evento,
                             @RequestParam(value = "validadoresIds", required = false) List<Long> validadoresIds,
                             @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
                             Authentication authentication) {
        Optional<Usuario> admin = usuarioService.obtenerPorEmail(authentication.getName());
        if (admin.isEmpty()){
            return "noexiste";
        }
        Usuario administrador = admin.get();
        List<Usuario> validadores = usuarioService.obtenerValidadoresPorIds(validadoresIds);
        // Al crear el evento se guarda también su organizador y validadores
        evento.setOrganizador(administrador);
        evento.setValidadores(validadores);
        evento.setImagenUrl(guardarImagenEvento(imagenFile));
        evento.setEstado(EstadoEvento.PLANIFICADO);
        evento.setCreadoEn(LocalDateTime.now());
        eventoService.guardar(evento);
        return "redirect:/admin/eventos";
    }
    @GetMapping("/admin/eventos/{id}/editar")
    public String editarEvento(@PathVariable Long id, Model model, Authentication authentication){
        Optional<Evento> eventoAEditar = eventoService.obtenerPorId(id);
        if (eventoAEditar.isEmpty()){
            return "noexiste";
        }
        model.addAttribute("evento", eventoAEditar.get());
        String adminLogueado = authentication.getName();
        Optional<Usuario> usuarioLogueado = usuarioService.obtenerPorEmail(adminLogueado);
        if (usuarioLogueado.isEmpty()){
            return "noexiste";
        }
        Usuario usuarioReal = usuarioLogueado.get();
        if (!usuarioReal.getId().equals(eventoAEditar.get().getOrganizador().getId())){
            return "noexiste";
        }
        List<Long> validadoresSeleccionadosIds = new ArrayList<Long>();
        if (eventoAEditar.get().getValidadores() != null) {
            for (Usuario validador : eventoAEditar.get().getValidadores()) {
                validadoresSeleccionadosIds.add(validador.getId());
            }
        }
        model.addAttribute("validadoresDisponibles", usuarioService.obtenerValidadores());
        model.addAttribute("validadoresSeleccionadosIds", validadoresSeleccionadosIds);
        return "admin/evento_form";
    }
    @PostMapping("/admin/eventos/{id}")
    public String postEvento(@PathVariable Long id,
                             @ModelAttribute Evento eventoEditado,
                             @RequestParam(value = "validadoresIds", required = false) List<Long> validadoresIds,
                             @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
                             Authentication authentication){
        Optional<Evento> eventoPost = eventoService.obtenerPorId(id);
        if (eventoPost.isEmpty()) {
            return "noexiste";
        }
        Evento eventoExistente = eventoPost.get();
        String adminLogueado = authentication.getName();
        Optional<Usuario> usuarioLogueado = usuarioService.obtenerPorEmail(adminLogueado);
        if (usuarioLogueado.isEmpty()){
            return "noexiste";
        }
        Usuario usuarioReal = usuarioLogueado.get();
        if (!usuarioReal.getId().equals(eventoPost.get().getOrganizador().getId())){
            return "noexiste";
        }
        eventoExistente.setNombre(eventoEditado.getNombre());
        eventoExistente.setDescripcion(eventoEditado.getDescripcion());
        eventoExistente.setUbicacion(eventoEditado.getUbicacion());
        eventoExistente.setFechaInicio(eventoEditado.getFechaInicio());
        eventoExistente.setFechaFin(eventoEditado.getFechaFin());
        eventoExistente.setCapacidad(eventoEditado.getCapacidad());
        eventoExistente.setPrecio(eventoEditado.getPrecio());
        eventoExistente.setTipo(eventoEditado.getTipo());
        // En edición se actualizan también los validadores y la imagen si cambia
        eventoExistente.setValidadores(usuarioService.obtenerValidadoresPorIds(validadoresIds));
        String imagenSubida = guardarImagenEvento(imagenFile);
        if (imagenSubida != null) {
            eventoExistente.setImagenUrl(imagenSubida);
        }
        eventoService.guardar(eventoExistente);
        return "redirect:/admin/eventos";
    }

    @PostMapping("/admin/eventos/{id}/publicar")
    public String publicarEvento(@PathVariable Long id, Authentication authentication) {
        Optional<Evento> eventoBuscado = eventoService.obtenerPorId(id);
        if (eventoBuscado.isEmpty()) {
            return "noexiste";
        }

        Optional<Usuario> usuarioLogueado = usuarioService.obtenerPorEmail(authentication.getName());
        if (usuarioLogueado.isEmpty()) {
            return "noexiste";
        }

        Evento evento = eventoBuscado.get();
        Usuario admin = usuarioLogueado.get();
        if (!admin.getId().equals(evento.getOrganizador().getId())) {
            return "noexiste";
        }

        evento.setEstado(EstadoEvento.PUBLICADO);
        eventoService.guardar(evento);
        return "redirect:/admin/eventos";
    }

    @PostMapping("/admin/eventos/{id}/ocultar")
    public String ocultarEvento(@PathVariable Long id, Authentication authentication) {
        Optional<Evento> eventoBuscado = eventoService.obtenerPorId(id);
        if (eventoBuscado.isEmpty()) {
            return "noexiste";
        }
        Optional<Usuario> usuarioLogueado = usuarioService.obtenerPorEmail(authentication.getName());
        if (usuarioLogueado.isEmpty()) {
            return "noexiste";
        }
        Evento evento = eventoBuscado.get();
        Usuario admin = usuarioLogueado.get();
        if (!admin.getId().equals(evento.getOrganizador().getId())) {
            return "noexiste";
        }
        evento.setEstado(EstadoEvento.PLANIFICADO);
        eventoService.guardar(evento);
        return "redirect:/admin/eventos";
    }

    @PostMapping("/admin/eventos/{id}/eliminar")
    public String eliminarEvento(@PathVariable Long id, Authentication authentication) {
        Optional<Evento> eventoBuscado = eventoService.obtenerPorId(id);
        if (eventoBuscado.isEmpty()){
            return "noexiste";
        }
        String adminLogueado = authentication.getName();
        Optional<Usuario> usuarioLogueado = usuarioService.obtenerPorEmail(adminLogueado);
        if (usuarioLogueado.isEmpty()){
            return "noexiste";
        }
        Usuario usuarioReal = usuarioLogueado.get();
        if (!usuarioReal.getId().equals(eventoBuscado.get().getOrganizador().getId())){
            return "noexiste";
        }
        eventoService.eliminar(id);
        return "redirect:/admin/eventos";
    }
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Authentication authentication, Model model){
        String mailAdmin = authentication.getName();
        Optional<Usuario> usuarioAdmin = usuarioService.obtenerPorEmail(mailAdmin);
        if (usuarioAdmin.isEmpty()){
            return "noexiste";
        }
        Usuario usuarioReal = usuarioAdmin.get();
        List<Evento> eventosAdmin = eventoService.obtenerPorOrganizador(usuarioReal);
        int totalEventos = eventosAdmin.size();
        int eventosPublicados = 0;
        int entradasVendidas = 0;
        int entradasUsadas = 0;

        List<Evento> eventosPublicadosList = new ArrayList<>();
        List<Entrada> todasLasEntradas = new ArrayList<>();
        List<Entrada> entradasValidadasList = new ArrayList<>();

        // Se recorren los eventos del admin para calcular estadísticas del panel
        for(Evento evento : eventosAdmin){
            if (evento.getEstado() == EstadoEvento.PUBLICADO){
                eventosPublicados++;
                eventosPublicadosList.add(evento);
            }

            List<Asistente> asistentesEvento = asistenteService.obtenerPorEvento(evento);
            for (Asistente asistente : asistentesEvento) {
                List<Entrada> entradasAsistente = entradaService.obtenerPorAsistente(asistente);
                todasLasEntradas.addAll(entradasAsistente);
                entradasVendidas += entradasAsistente.size();
                for (Entrada entrada : entradasAsistente) {
                    if (entrada.getEstado() == EstadoEntrada.USADA) {
                        entradasUsadas++;
                        entradasValidadasList.add(entrada);
                    }
                }
            }
        }

        model.addAttribute("totalEventos", totalEventos);
        model.addAttribute("eventosPublicados", eventosPublicados);
        model.addAttribute("entradasVendidas", entradasVendidas);
        model.addAttribute("entradasUsadas", entradasUsadas);
        model.addAttribute("eventosAdmin", eventosAdmin);
        model.addAttribute("eventosPublicadosList", eventosPublicadosList);
        model.addAttribute("todasLasEntradas", todasLasEntradas);
        model.addAttribute("entradasValidadasList", entradasValidadasList);
        return "admin/dashboard";
    }

    private String guardarImagenEvento(MultipartFile imagenFile) {
        try {
            return supabaseStorageService.subirImagen(imagenFile);
        } catch (Exception e) {
            // Si la subida falla, no se bloquea la creación del evento
            return null;
        }
    }    
}
