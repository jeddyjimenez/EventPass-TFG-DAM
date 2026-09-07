package com.tfg.eventos.repositorio;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositorio de Evento, contiene sus métodos de búsqueda personalizados.

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByOrganizador(Usuario organizador);
}