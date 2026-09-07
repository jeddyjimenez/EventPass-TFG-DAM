package com.tfg.eventos.servicio;
import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.repositorio.AsistenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Clase servicio de Asistente, contiene la lógica de negocio relacionada con asistentes.

@Service
public class AsistenteService {
    @Autowired
    private AsistenteRepository asistenteRepository;

    public List<Asistente> obtenerTodos() {
        return asistenteRepository.findAll();
    }

    public Optional<Asistente> obtenerPorId(Long id) {
        return asistenteRepository.findById(id);
    }

    public List<Asistente> obtenerPorEvento(Evento evento) {
        return asistenteRepository.findByEvento(evento);
    }

    public Optional<Asistente> obtenerPorUsuarioYEvento(Usuario usuario, Evento evento) {
        return asistenteRepository.findByUsuarioAndEvento(usuario, evento);
    }

    public Asistente guardar(Asistente asistente) {
        return asistenteRepository.save(asistente);
    }

    public Asistente actualizar(Asistente asistente) {
        return asistenteRepository.save(asistente);
    }

    public void eliminar(Long id) {
        asistenteRepository.deleteById(id);
    }
}