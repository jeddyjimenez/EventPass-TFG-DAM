package com.tfg.eventos.controlador;

import com.tfg.eventos.servicio.EventoService;
import com.tfg.eventos.servicio.EntradaService;
import com.tfg.eventos.servicio.RegistroAccesoService;
import com.tfg.eventos.servicio.UsuarioService;

import org.springframework.web.bind.annotation.RestController;

import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.RegistroAcceso;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.EstadoEntrada;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class ValidadorApiController {
    private final EntradaService entradaService;
    private final RegistroAccesoService registroAccesoService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;

    ValidadorApiController(EntradaService entradaService,
                           RegistroAccesoService registroAccesoService,
                           UsuarioService usuarioService,
                           EventoService eventoService) {
        this.entradaService = entradaService;
        this.registroAccesoService = registroAccesoService;
        this.usuarioService = usuarioService;
        this.eventoService = eventoService;
    }

    @GetMapping("/api/validador/eventos")
    public List<EventoAsignadoResponse> obtenerEventosAsignados(Authentication authentication) {
        List<EventoAsignadoResponse> respuesta = new ArrayList<>();

        if (authentication == null || authentication.getName() == null) {
            return respuesta;
        }

        Optional<Usuario> validadorLogueado = usuarioService.obtenerPorEmail(authentication.getName());
        if (validadorLogueado.isEmpty()) {
            return respuesta;
        }

        List<Evento> eventos = eventoService.obtenerTodos();
        // Solo se devuelven los eventos donde el usuario actúa como validador
        for (Evento evento : eventos) {
            if (evento.getValidadores() == null) {
                continue;
            }

            boolean asignado = false;
            for (Usuario validador : evento.getValidadores()) {
                if (validador.getId().equals(validadorLogueado.get().getId())) {
                    asignado = true;
                    break;
                }
            }

            if (asignado) {
                EventoAsignadoResponse eventoResponse = new EventoAsignadoResponse();
                eventoResponse.setId(evento.getId());
                eventoResponse.setNombre(evento.getNombre());
                eventoResponse.setUbicacion(evento.getUbicacion());
                eventoResponse.setFechaInicio(evento.getFechaInicio());
                eventoResponse.setFechaFin(evento.getFechaFin());
                respuesta.add(eventoResponse);
            }
        }

        return respuesta;
    }

    @PostMapping("/api/validador/validar")
    public Map<String, String> validarEntradaApi(@RequestBody ValidacionRequest request, Authentication authentication) {
        Map<String, String> respuesta = new HashMap<>();

        // Se comprueba primero que vengan los datos mínimos para validar
        if (request == null || request.getQrToken() == null || request.getQrToken().isBlank() || request.getIdEvento() == null) {
            respuesta.put("estado", "datosinvalidos");
            respuesta.put("mensaje", "Faltan datos para validar.");
            return respuesta;
        }

        Optional<Usuario> validadorLogueado = usuarioService.obtenerPorEmail(authentication.getName());
        if (validadorLogueado.isEmpty()) {
            respuesta.put("estado", "noautorizado");
            respuesta.put("mensaje", "Validador no encontrado.");
            return respuesta;
        }

        Optional<Evento> eventoSeleccionado = eventoService.obtenerPorId(request.getIdEvento());
        if (eventoSeleccionado.isEmpty()) {
            respuesta.put("estado", "eventonoexiste");
            respuesta.put("mensaje", "El evento seleccionado no existe.");
            return respuesta;
        }

        Evento evento = eventoSeleccionado.get();
        boolean validadorAsignado = false;
        // El validador solo puede validar entradas de los eventos asignados
        if (evento.getValidadores() != null) {
            for (Usuario validador : evento.getValidadores()) {
                if (validador.getId().equals(validadorLogueado.get().getId())) {
                    validadorAsignado = true;
                    break;
                }
            }
        }
        if (!validadorAsignado) {
            respuesta.put("estado", "noasignado");
            respuesta.put("mensaje", "No tienes permiso para validar este evento.");
            return respuesta;
        }

        Optional<Entrada> entradaABuscar = entradaService.obtenerPorQrToken(request.getQrToken());

        if (entradaABuscar.isEmpty()){
            respuesta.put("estado", "noexiste");
            respuesta.put("mensaje", "Esta entrada no existe.");
            return respuesta;
        }

        Entrada entradaObtenida = entradaABuscar.get();
        if (!entradaObtenida.getAsistente().getEvento().getId().equals(evento.getId())) {
            respuesta.put("estado", "eventoincorrecto");
            respuesta.put("mensaje", "La entrada no pertenece al evento seleccionado.");
            return respuesta;
        }

        if (entradaObtenida.getEstado() == EstadoEntrada.USADA){
            respuesta.put("estado", "yausada");
            respuesta.put("mensaje", "La entrada ya ha sido utilizada.");
            return respuesta;
        }
        if (entradaObtenida.getEstado() == EstadoEntrada.CANCELADA){
            respuesta.put("estado", "cancelada");
            respuesta.put("mensaje", "La entrada ha sido cancelada.");
            return respuesta;
        }
        // Si pasa todas las comprobaciones, la entrada se marca como usada
        entradaObtenida.setEstado(EstadoEntrada.USADA);
        entradaObtenida.setUsadaEn(LocalDateTime.now());
        entradaService.guardar(entradaObtenida);
        // Se registra el acceso para dejar trazabilidad en base de datos
        RegistroAcceso registro = new RegistroAcceso();
        registro.setEntrada(entradaObtenida);
        registro.setFechaAcceso(LocalDateTime.now());
        registro.setObservaciones("Entrada validada.");
        registroAccesoService.guardar(registro);
        respuesta.put("estado", "valida");
        respuesta.put("mensaje", "Entrada validada correctamente");
        return respuesta;
    }

    public static class ValidacionRequest {
        private String qrToken;
        private Long idEvento;

        public String getQrToken() {
            return qrToken;
        }

        public void setQrToken(String qrToken) {
            this.qrToken = qrToken;
        }

        public Long getIdEvento() {
            return idEvento;
        }

        public void setIdEvento(Long idEvento) {
            this.idEvento = idEvento;
        }
    }

    public static class EventoAsignadoResponse {
        private Long id;
        private String nombre;
        private String ubicacion;
        private LocalDateTime fechaInicio;
        private LocalDateTime fechaFin;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }

        public LocalDateTime getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public LocalDateTime getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(LocalDateTime fechaFin) {
            this.fechaFin = fechaFin;
        }
    }
}
