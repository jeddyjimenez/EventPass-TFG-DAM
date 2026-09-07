package com.tfg.eventos.repositorio;
import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// Repositorio de Asistente, contiene sus métodos de búsqueda personalizados.

public interface AsistenteRepository extends JpaRepository<Asistente, Long> {
    List<Asistente> findByEvento(Evento evento);
    Optional<Asistente> findByUsuarioAndEvento(Usuario usuario, Evento evento);
}