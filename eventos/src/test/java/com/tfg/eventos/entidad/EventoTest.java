package com.tfg.eventos.entidad;

import com.tfg.eventos.entidad.enums.EstadoEvento;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventoTest {

    @Test
    void constructorCompleto_deberiaInicializarCamposCorrectos() {
        // Arrange
        Usuario organizador = new Usuario();
        organizador.setId(1L);
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 1, 10, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 6, 1, 22, 0);

        // Act
        Evento evento = new Evento("Festival", "Descripción", inicio, fin, "Madrid", 500, organizador);

        // Assert
        assertThat(evento.getNombre()).isEqualTo("Festival");
        assertThat(evento.getDescripcion()).isEqualTo("Descripción");
        assertThat(evento.getFechaInicio()).isEqualTo(inicio);
        assertThat(evento.getFechaFin()).isEqualTo(fin);
        assertThat(evento.getUbicacion()).isEqualTo("Madrid");
        assertThat(evento.getCapacidad()).isEqualTo(500);
        assertThat(evento.getOrganizador()).isEqualTo(organizador);
        assertThat(evento.getEstado()).isEqualTo(EstadoEvento.PLANIFICADO);
        assertThat(evento.getCreadoEn()).isNotNull();
    }

    @Test
    void constructorVacio_deberiaCrearInstanciasSinExcepcion() {
        // Act
        Evento evento = new Evento();

        // Assert
        assertThat(evento).isNotNull();
    }

    @Test
    void settersYGetters_deberianFuncionar() {
        // Arrange
        Evento evento = new Evento();
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        evento.setId(99L);
        evento.setNombre("Test");
        evento.setDescripcion("Desc");
        evento.setFechaInicio(ahora);
        evento.setFechaFin(ahora.plusHours(4));
        evento.setUbicacion("Barcelona");
        evento.setCapacidad(200);
        evento.setImagenUrl("imagen.png");
        evento.setEstado(EstadoEvento.PUBLICADO);
        evento.setCreadoEn(ahora);

        // Assert
        assertThat(evento.getId()).isEqualTo(99L);
        assertThat(evento.getNombre()).isEqualTo("Test");
        assertThat(evento.getDescripcion()).isEqualTo("Desc");
        assertThat(evento.getFechaInicio()).isEqualTo(ahora);
        assertThat(evento.getFechaFin()).isEqualTo(ahora.plusHours(4));
        assertThat(evento.getUbicacion()).isEqualTo("Barcelona");
        assertThat(evento.getCapacidad()).isEqualTo(200);
        assertThat(evento.getImagenUrl()).isEqualTo("imagen.png");
        assertThat(evento.getEstado()).isEqualTo(EstadoEvento.PUBLICADO);
        assertThat(evento.getCreadoEn()).isEqualTo(ahora);
    }
}
