package com.tfg.eventos.servicio;

import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.entidad.enums.RolUsuario;
import com.tfg.eventos.repositorio.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioAdmin;
    private Usuario usuarioValidador;
    private Usuario usuarioNormal;

    @BeforeEach
    void setUp() {
        usuarioAdmin = new Usuario("admin@test.com", "Admin", "hash1", RolUsuario.ADMIN);
        usuarioAdmin.setId(1L);

        usuarioValidador = new Usuario("validador@test.com", "Validador", "hash2", RolUsuario.VALIDADOR);
        usuarioValidador.setId(2L);

        usuarioNormal = new Usuario("usuario@test.com", "Usuario", "hash3", RolUsuario.USUARIO);
        usuarioNormal.setId(3L);
    }

    @Test
    void obtenerTodos_deberiaRetornarTodosLosUsuarios() {
        // Arrange
        List<Usuario> lista = Arrays.asList(usuarioAdmin, usuarioValidador, usuarioNormal);
        when(usuarioRepository.findAll()).thenReturn(lista);

        // Act
        List<Usuario> resultado = usuarioService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(3);
        verify(usuarioRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioAdmin));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorId(1L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("admin@test.com");
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorId(99L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorEmail_cuandoExiste_deberiaRetornarUsuario() {
        // Arrange
        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(usuarioAdmin));

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorEmail("admin@test.com");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getRol()).isEqualTo(RolUsuario.ADMIN);
        verify(usuarioRepository).findByEmail("admin@test.com");
    }

    @Test
    void obtenerPorEmail_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(usuarioRepository.findByEmail("noexiste@test.com")).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioService.obtenerPorEmail("noexiste@test.com");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerValidadores_deberiaRetornarSoloValidadores() {
        // Arrange
        when(usuarioRepository.findByRol(RolUsuario.VALIDADOR)).thenReturn(Arrays.asList(usuarioValidador));

        // Act
        List<Usuario> resultado = usuarioService.obtenerValidadores();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRol()).isEqualTo(RolUsuario.VALIDADOR);
        verify(usuarioRepository).findByRol(RolUsuario.VALIDADOR);
    }

    @Test
    void obtenerValidadoresPorIds_conListaNula_deberiaRetornarListaVacia() {
        // Act
        List<Usuario> resultado = usuarioService.obtenerValidadoresPorIds(null);

        // Assert
        assertThat(resultado).isEmpty();
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void obtenerValidadoresPorIds_conListaVacia_deberiaRetornarListaVacia() {
        // Act
        List<Usuario> resultado = usuarioService.obtenerValidadoresPorIds(Collections.emptyList());

        // Assert
        assertThat(resultado).isEmpty();
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void obtenerValidadoresPorIds_filtraSoloValidadores() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        // El repositorio devuelve un admin, un validador y un usuario normal
        when(usuarioRepository.findAllById(ids))
                .thenReturn(Arrays.asList(usuarioAdmin, usuarioValidador, usuarioNormal));

        // Act
        List<Usuario> resultado = usuarioService.obtenerValidadoresPorIds(ids);

        // Assert
        // Solo el validador debe ser devuelto
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getRol()).isEqualTo(RolUsuario.VALIDADOR);
        verify(usuarioRepository).findAllById(ids);
    }

    @Test
    void obtenerValidadoresPorIds_cuandoTodosValidadores_deberiaRetornarTodos() {
        // Arrange
        Usuario otroValidador = new Usuario("v2@test.com", "Validador2", "hash4", RolUsuario.VALIDADOR);
        otroValidador.setId(4L);
        List<Long> ids = Arrays.asList(2L, 4L);
        when(usuarioRepository.findAllById(ids))
                .thenReturn(Arrays.asList(usuarioValidador, otroValidador));

        // Act
        List<Usuario> resultado = usuarioService.obtenerValidadoresPorIds(ids);

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(u -> u.getRol() == RolUsuario.VALIDADOR);
    }

    @Test
    void obtenerValidadoresPorIds_cuandoNingunoValidador_deberiaRetornarListaVacia() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 3L);
        when(usuarioRepository.findAllById(ids))
                .thenReturn(Arrays.asList(usuarioAdmin, usuarioNormal));

        // Act
        List<Usuario> resultado = usuarioService.obtenerValidadoresPorIds(ids);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void guardar_deberiaRetornarUsuarioGuardado() {
        // Arrange
        when(usuarioRepository.save(usuarioAdmin)).thenReturn(usuarioAdmin);

        // Act
        Usuario resultado = usuarioService.guardar(usuarioAdmin);

        // Assert
        assertThat(resultado).isEqualTo(usuarioAdmin);
        verify(usuarioRepository).save(usuarioAdmin);
    }

    @Test
    void actualizar_deberiaRetornarUsuarioActualizado() {
        // Arrange
        usuarioAdmin.setNombre("Admin Actualizado");
        when(usuarioRepository.save(usuarioAdmin)).thenReturn(usuarioAdmin);

        // Act
        Usuario resultado = usuarioService.actualizar(usuarioAdmin);

        // Assert
        assertThat(resultado.getNombre()).isEqualTo("Admin Actualizado");
        verify(usuarioRepository).save(usuarioAdmin);
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        // Arrange
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act
        usuarioService.eliminar(1L);

        // Assert
        verify(usuarioRepository).deleteById(1L);
    }
}
