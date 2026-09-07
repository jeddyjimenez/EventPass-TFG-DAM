package com.tfg.eventos.servicio;
import com.tfg.eventos.entidad.RegistroAcceso;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.repositorio.RegistroAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Clase servicio de RegistroAcceso, contiene la lógica de negocio relacionada con registros de acceso.

@Service
public class RegistroAccesoService {
    @Autowired
    private RegistroAccesoRepository registroAccesoRepository;

    public List<RegistroAcceso> obtenerTodos() {
        return registroAccesoRepository.findAll();
    }

    public Optional<RegistroAcceso> obtenerPorId(Long id) {
        return registroAccesoRepository.findById(id);
    }

    public List<RegistroAcceso> obtenerPorEntrada(Entrada entrada) {
        return registroAccesoRepository.findByEntrada(entrada);
    }

    public RegistroAcceso guardar(RegistroAcceso registroAcceso) {
        return registroAccesoRepository.save(registroAcceso);
    }

    public RegistroAcceso actualizar(RegistroAcceso registroAcceso) {
        return registroAccesoRepository.save(registroAcceso);
    }

    public void eliminar(Long id) {
        registroAccesoRepository.deleteById(id);
    }
}