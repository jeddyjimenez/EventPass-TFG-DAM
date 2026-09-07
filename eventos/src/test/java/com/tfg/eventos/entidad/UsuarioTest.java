package com.tfg.eventos.entidad;

import com.tfg.eventos.entidad.enums.RolUsuario;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioTest {

    @Test
    void constructorCompleto_deberiaInicializarCamposCorrectos() {
        // Act
        Usuario usuario = new Usuario("test@test.com", "Nombre Test", "hashed_pw", RolUsuario.USUARIO);

        // Assert
        assertThat(usuario.getEmail()).isEqualTo("test@test.com");
        assertThat(usuario.getNombre()).isEqualTo("Nombre Test");
        assertThat(usuario.getContrasenaCifrada()).isEqualTo("hashed_pw");
        assertThat(usuario.getRol()).isEqualTo(RolUsuario.USUARIO);
        assertThat(usuario.getCreadoEn()).isNotNull();
    }

    @Test
    void constructorVacio_deberiaCrearInstanciasSinExcepcion() {
        // Act
        Usuario usuario = new Usuario();

        // Assert
        assertThat(usuario).isNotNull();
    }

    @Test
    void settersYGetters_deberianFuncionar() {
        // Arrange
        Usuario usuario = new Usuario();

        // Act
        usuario.setId(5L);
        usuario.setEmail("otro@test.com");
        usuario.setNombre("Otro Usuario");
        usuario.setContrasenaCifrada("nuevo_hash");
        usuario.setRol(RolUsuario.ADMIN);

        // Assert
        assertThat(usuario.getId()).isEqualTo(5L);
        assertThat(usuario.getEmail()).isEqualTo("otro@test.com");
        assertThat(usuario.getNombre()).isEqualTo("Otro Usuario");
        assertThat(usuario.getContrasenaCifrada()).isEqualTo("nuevo_hash");
        assertThat(usuario.getRol()).isEqualTo(RolUsuario.ADMIN);
    }

    @Test
    void constructorConRolValidador_deberiaAsignarRolValidador() {
        // Act
        Usuario validador = new Usuario("v@test.com", "Validador", "pw", RolUsuario.VALIDADOR);

        // Assert
        assertThat(validador.getRol()).isEqualTo(RolUsuario.VALIDADOR);
    }
}
