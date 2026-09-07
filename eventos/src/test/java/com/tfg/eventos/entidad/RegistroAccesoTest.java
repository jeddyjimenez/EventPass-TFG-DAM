package com.tfg.eventos.entidad;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RegistroAccesoTest {

    @Test
    void constructorCompleto_deberiaInicializarCamposCorrectos() {
        // Arrange
        Entrada entrada = new Entrada();
        entrada.setId(5L);
        LocalDateTime fechaAcceso = LocalDateTime.of(2026, 6, 1, 20, 30);

        // Act
        RegistroAcceso registro = new RegistroAcceso(entrada, fechaAcceso, "Acceso válido");

        // Assert
        assertThat(registro.getEntrada()).isEqualTo(entrada);
        assertThat(registro.getFechaAcceso()).isEqualTo(fechaAcceso);
        assertThat(registro.getObservaciones()).isEqualTo("Acceso válido");
    }

    @Test
    void constructorVacio_deberiaCrearInstanciasSinExcepcion() {
        // Act
        RegistroAcceso registro = new RegistroAcceso();

        // Assert
        assertThat(registro).isNotNull();
    }

    @Test
    void settersYGetters_deberianFuncionar() {
        // Arrange
        RegistroAcceso registro = new RegistroAcceso();
        Entrada entrada = new Entrada();
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        registro.setId(50L);
        registro.setEntrada(entrada);
        registro.setFechaAcceso(ahora);
        registro.setObservaciones("Sin novedades");

        // Assert
        assertThat(registro.getId()).isEqualTo(50L);
        assertThat(registro.getEntrada()).isEqualTo(entrada);
        assertThat(registro.getFechaAcceso()).isEqualTo(ahora);
        assertThat(registro.getObservaciones()).isEqualTo("Sin novedades");
    }

    @Test
    void observaciones_puedeSerNulo() {
        // Arrange
        Entrada entrada = new Entrada();

        // Act
        RegistroAcceso registro = new RegistroAcceso(entrada, LocalDateTime.now(), null);

        // Assert
        assertThat(registro.getObservaciones()).isNull();
    }
}
