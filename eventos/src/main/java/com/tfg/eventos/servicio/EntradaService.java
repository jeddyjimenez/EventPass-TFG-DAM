package com.tfg.eventos.servicio;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.repositorio.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Clase servicio de Entrada, contiene la lógica de negocio relacionada con entradas.

@Service
public class EntradaService {
    @Autowired
    private EntradaRepository entradaRepository;

    public List<Entrada> obtenerTodos() {
        return entradaRepository.findAll();
    }

    public Optional<Entrada> obtenerPorId(Long id) {
        return entradaRepository.findById(id);
    }

    public Optional<Entrada> obtenerPorQrToken(String qrToken) {
        return entradaRepository.findByQrToken(qrToken);
    }

    public List<Entrada> obtenerPorAsistente(Asistente asistente) {
        return entradaRepository.findByAsistente(asistente);
    }

    public boolean existeEntradaActivaPorUsuarioYEvento(Usuario usuario, Evento evento) {
        return entradaRepository.existsByAsistenteUsuarioAndAsistenteEventoAndEstado(usuario, evento, EstadoEntrada.ACTIVA);
    }

    public Entrada guardar(Entrada entrada) {
        return entradaRepository.save(entrada);
    }

    public Entrada actualizar(Entrada entrada) {
        return entradaRepository.save(entrada);
    }

    public void eliminar(Long id) {
        entradaRepository.deleteById(id);
    }
}
