package com.tfg.eventos.entidad;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AsistenteTest {

    @Test
    void constructorCompleto_deberiaInicializarCamposCorrectos() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Evento evento = new Evento();
        evento.setId(10L);

        // Act
        Asistente asistente = new Asistente(usuario, evento);

        // Assert
        assertThat(asistente.getUsuario()).isEqualTo(usuario);
        assertThat(asistente.getEvento()).isEqualTo(evento);
        assertThat(asistente.getRegistradoEn()).isNotNull();
    }

    @Test
    void constructorVacio_deberiaCrearInstanciasSinExcepcion() {
        // Act
        Asistente asistente = new Asistente();

        // Assert
        assertThat(asistente).isNotNull();
    }

    @Test
    void settersYGetters_deberianFuncionar() {
        // Arrange
        Asistente asistente = new Asistente();
        LocalDateTime ahora = LocalDateTime.now();
        Usuario usuario = new Usuario();
        Evento evento = new Evento();

        // Act
        asistente.setId(30L);
        asistente.setUsuario(usuario);
        asistente.setEvento(evento);
        asistente.setRegistradoEn(ahora);

        // Assert
        assertThat(asistente.getId()).isEqualTo(30L);
        assertThat(asistente.getUsuario()).isEqualTo(usuario);
        assertThat(asistente.getEvento()).isEqualTo(evento);
        assertThat(asistente.getRegistradoEn()).isEqualTo(ahora);
    }
}
