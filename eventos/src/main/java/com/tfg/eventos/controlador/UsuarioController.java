package com.tfg.eventos.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.servicio.AsistenteService;
import com.tfg.eventos.servicio.EntradaService;
import com.tfg.eventos.servicio.QrService;
import com.tfg.eventos.servicio.UsuarioService;

@Controller
public class UsuarioController {
    private final QrService qrService;
    private final UsuarioService usuarioService;
    private final AsistenteService asistenteService;
    private final EntradaService entradaService;
    public UsuarioController(UsuarioService usuarioService, AsistenteService asistenteService, EntradaService entradaService, QrService qrService){
        this.usuarioService = usuarioService;
        this.asistenteService = asistenteService;
        this.entradaService = entradaService;
        this.qrService = qrService;
    }
    @GetMapping("/mis_entradas")
    public String obtenerEntradas(Model model, Authentication authentication){
        String emailLogueado = authentication.getName();
        Optional<Usuario> usuario = usuarioService.obtenerPorEmail(emailLogueado);
        if (usuario.isEmpty()){
            return "noexiste";
        }
        Usuario usuarioReal = usuario.get();
        List<Asistente> asistentes = asistenteService.obtenerTodos();
        List<Asistente> asistentesUsuario = new ArrayList<>();
        // Se buscan las relaciones asistente del usuario logueado
        for (Asistente asistente : asistentes){
            if (asistente.getUsuario().getId() == usuarioReal.getId()){
                asistentesUsuario.add(asistente);
            }
        }
        List<Entrada> entradas = new ArrayList<>();
        // A partir de cada asistente se obtienen sus entradas
        for (Asistente as : asistentesUsuario){
            List<Entrada> entradasAsistente = entradaService.obtenerPorAsistente(as);
            entradas.addAll(entradasAsistente);
        }
        model.addAttribute("entradas", entradas);
        return "mis_entradas";
        }

    @PostMapping("/mis_entradas/{id}/eliminar")
    public String eliminarEntrada(@PathVariable Long id, Authentication authentication) {
        Optional<Entrada> entradaOpt = entradaService.obtenerPorId(id);
        if (entradaOpt.isEmpty()) {
            return "redirect:/mis_entradas";
        }
        Entrada entrada = entradaOpt.get();
        if (!entrada.getAsistente().getUsuario().getEmail().equals(authentication.getName())) {
            return "redirect:/mis_entradas";
        }
        entradaService.eliminar(id);
        return "redirect:/mis_entradas";
    }

    @GetMapping(value="/entradas/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] verQr(@PathVariable Long id, Authentication auth) {
        Entrada entrada = entradaService.obtenerPorId(id).orElseThrow();
        // Genera y devuelve la imagen PNG del código QR de la entrada
        return qrService.generarPng(entrada.getQrToken(), 320, 320);
     }
        

    @GetMapping("/api/cliente/entradas")
    @ResponseBody
    public List<Map<String, Object>> obtenerEntradasApi(Authentication authentication) {
        String emailLogueado = authentication.getName();
        Optional<Usuario> usuario = usuarioService.obtenerPorEmail(emailLogueado);

        List<Map<String, Object>> resultado = new ArrayList<>();

        if (usuario.isEmpty()) {
            return resultado;
        }

        Usuario usuarioReal = usuario.get();
        List<Asistente> asistentes = asistenteService.obtenerTodos();

        // Se prepara una respuesta simple para la app móvil
        for (Asistente asistente : asistentes) {
            if (asistente.getUsuario().getId().equals(usuarioReal.getId())) {
                List<Entrada> entradasAsistente = entradaService.obtenerPorAsistente(asistente);

                for (Entrada entrada : entradasAsistente) {
                    Map<String, Object> fila = new HashMap<>();
                    fila.put("id", entrada.getId());
                    fila.put("nombreEvento", entrada.getAsistente().getEvento().getNombre());
                    fila.put("estado", entrada.getEstado().name());
                    fila.put("estadoPago", entrada.getEstadoPago().name());
                    fila.put("compradaEn", entrada.getCompradaEn());
                    resultado.add(fila);
                }
            }
        }

        return resultado;
    }
}
