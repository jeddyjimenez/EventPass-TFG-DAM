package com.tfg.eventos.servicio;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.RolUsuario;
import com.tfg.eventos.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

// Clase servicio de Usuario, contiene la lógica de negocio relacionada con usuarios.

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> obtenerValidadores() {
        return usuarioRepository.findByRol(RolUsuario.VALIDADOR);
    }

    public List<Usuario> obtenerValidadoresPorIds(List<Long> idsValidadores) {
        List<Usuario> validadores = new ArrayList<Usuario>();
        if (idsValidadores == null || idsValidadores.isEmpty()) {
            return validadores;
        }

        List<Usuario> usuarios = usuarioRepository.findAllById(idsValidadores);
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() == RolUsuario.VALIDADOR) {
                validadores.add(usuario);
            }
        }
        return validadores;
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
