package com.tfg.eventos.repositorio;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repositorio de Entrada, contiene sus métodos de búsqueda personalizados.

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    Optional<Entrada> findByQrToken(String qrToken);
    List<Entrada> findByAsistente(Asistente asistente);
    boolean existsByAsistenteUsuarioAndAsistenteEventoAndEstado(Usuario usuario, Evento evento, EstadoEntrada estado);
}
