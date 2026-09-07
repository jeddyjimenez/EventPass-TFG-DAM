package com.tfg.eventos.repositorio;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// Repositorio de Usuario, contiene sus métodos de búsqueda personalizados.

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(RolUsuario rol);
}
