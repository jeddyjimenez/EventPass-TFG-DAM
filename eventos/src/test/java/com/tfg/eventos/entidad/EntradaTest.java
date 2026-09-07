package com.tfg.eventos.entidad;

import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.entidad.enums.EstadoPago;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EntradaTest {

    @Test
    void constructorCompleto_deberiaInicializarCamposCorrectos() {
        // Arrange
        Asistente asistente = new Asistente();
        asistente.setId(1L);
        LocalDateTime compradaEn = LocalDateTime.of(2026, 5, 1, 12, 0);

        // Act
        Entrada entrada = new Entrada("QR-TOKEN-001", EstadoEntrada.ACTIVA, EstadoPago.PAGADO,
                compradaEn, asistente);

        // Assert
        assertThat(entrada.getQrToken()).isEqualTo("QR-TOKEN-001");
        assertThat(entrada.getEstado()).isEqualTo(EstadoEntrada.ACTIVA);
        assertThat(entrada.getEstadoPago()).isEqualTo(EstadoPago.PAGADO);
        assertThat(entrada.getCompradaEn()).isEqualTo(compradaEn);
        assertThat(entrada.getAsistente()).isEqualTo(asistente);
    }

    @Test
    void constructorVacio_deberiaCrearInstanciasSinExcepcion() {
        // Act
        Entrada entrada = new Entrada();

        // Assert
        assertThat(entrada).isNotNull();
    }

    @Test
    void settersYGetters_deberianFuncionar() {
        // Arrange
        Entrada entrada = new Entrada();
        LocalDateTime ahora = LocalDateTime.now();

        // Act
        entrada.setId(7L);
        entrada.setQrToken("QR-XYZ");
        entrada.setEstado(EstadoEntrada.USADA);
        entrada.setEstadoPago(EstadoPago.CANCELADO);
        entrada.setCompradaEn(ahora);
        entrada.setUsadaEn(ahora.plusDays(1));

        // Assert
        assertThat(entrada.getId()).isEqualTo(7L);
        assertThat(entrada.getQrToken()).isEqualTo("QR-XYZ");
        assertThat(entrada.getEstado()).isEqualTo(EstadoEntrada.USADA);
        assertThat(entrada.getEstadoPago()).isEqualTo(EstadoPago.CANCELADO);
        assertThat(entrada.getCompradaEn()).isEqualTo(ahora);
        assertThat(entrada.getUsadaEn()).isEqualTo(ahora.plusDays(1));
    }
}
