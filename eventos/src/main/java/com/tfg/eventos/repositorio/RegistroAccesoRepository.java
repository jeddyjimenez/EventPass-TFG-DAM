package com.tfg.eventos.repositorio;
import com.tfg.eventos.entidad.RegistroAcceso;
import com.tfg.eventos.entidad.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositorio de RegistroAcceso, contiene sus métodos de búsqueda personalizados.

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {
    List<RegistroAcceso> findByEntrada(Entrada entrada);
}