package com.tfg.eventos.servicio;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.EstadoEvento;
import com.tfg.eventos.entidad.enums.TipoEvento;
import com.tfg.eventos.repositorio.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Clase servicio de Evento, contiene la lógica de negocio relacionada con eventos.

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;

    public List<Evento> obtenerTodos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> obtenerPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public List<Evento> obtenerPorOrganizador(Usuario organizador) {
        return eventoRepository.findByOrganizador(organizador);
    }

    public Evento guardar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Evento actualizar(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void eliminar(Long id) {
        eventoRepository.deleteById(id);
    }

    public List<Evento> buscar(String nombre, TipoEvento tipo, LocalDate fechaDesde, LocalDate fechaHasta, String ciudad) {
        // La búsqueda aplica filtros solo sobre eventos publicados
        return eventoRepository.findAll().stream()
            .filter(e -> e.getEstado() == EstadoEvento.PUBLICADO)
            .filter(e -> nombre == null || nombre.isBlank() ||
                    e.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(e -> tipo == null || (e.getTipo() != null && e.getTipo() == tipo))
            .filter(e -> fechaDesde == null ||
                    !e.getFechaInicio().toLocalDate().isBefore(fechaDesde))
            .filter(e -> fechaHasta == null ||
                    !e.getFechaInicio().toLocalDate().isAfter(fechaHasta))
            .filter(e -> ciudad == null || ciudad.isBlank() ||
                    e.getUbicacion().toLowerCase().contains(ciudad.toLowerCase()))
            .collect(Collectors.toList());
    }
}