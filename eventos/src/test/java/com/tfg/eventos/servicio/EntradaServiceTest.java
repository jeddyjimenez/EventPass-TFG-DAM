package com.tfg.eventos.servicio;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.entidad.enums.EstadoPago;
import com.tfg.eventos.repositorio.EntradaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntradaServiceTest {

    @Mock
    private EntradaRepository entradaRepository;

    @InjectMocks
    private EntradaService entradaService;

    private Entrada entrada;
    private Asistente asistente;

    @BeforeEach
    void setUp() {
        asistente = new Asistente();
        asistente.setId(1L);

        entrada = new Entrada("TOKEN-ABC123", EstadoEntrada.ACTIVA, EstadoPago.PAGADO,
                LocalDateTime.now(), asistente);
        entrada.setId(5L);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeEntradas() {
        // Arrange
        when(entradaRepository.findAll()).thenReturn(Arrays.asList(entrada));

        // Act
        List<Entrada> resultado = entradaService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(1);
        verify(entradaRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarEntrada() {
        // Arrange
        when(entradaRepository.findById(5L)).thenReturn(Optional.of(entrada));

        // Act
        Optional<Entrada> resultado = entradaService.obtenerPorId(5L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(5L);
        verify(entradaRepository).findById(5L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(entradaRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Entrada> resultado = entradaService.obtenerPorId(99L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorQrToken_cuandoTokenExiste_deberiaRetornarEntrada() {
        // Arrange
        when(entradaRepository.findByQrToken("TOKEN-ABC123")).thenReturn(Optional.of(entrada));

        // Act
        Optional<Entrada> resultado = entradaService.obtenerPorQrToken("TOKEN-ABC123");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getQrToken()).isEqualTo("TOKEN-ABC123");
        verify(entradaRepository).findByQrToken("TOKEN-ABC123");
    }

    @Test
    void obtenerPorQrToken_cuandoTokenNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(entradaRepository.findByQrToken("TOKEN-INVALIDO")).thenReturn(Optional.empty());

        // Act
        Optional<Entrada> resultado = entradaService.obtenerPorQrToken("TOKEN-INVALIDO");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorAsistente_deberiaRetornarEntradasDelAsistente() {
        // Arrange
        when(entradaRepository.findByAsistente(asistente)).thenReturn(Arrays.asList(entrada));

        // Act
        List<Entrada> resultado = entradaService.obtenerPorAsistente(asistente);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getAsistente()).isEqualTo(asistente);
        verify(entradaRepository).findByAsistente(asistente);
    }

    @Test
    void guardar_deberiaRetornarEntradaGuardada() {
        // Arrange
        when(entradaRepository.save(entrada)).thenReturn(entrada);

        // Act
        Entrada resultado = entradaService.guardar(entrada);

        // Assert
        assertThat(resultado).isEqualTo(entrada);
        verify(entradaRepository).save(entrada);
    }

    @Test
    void actualizar_deberiaRetornarEntradaActualizada() {
        // Arrange
        entrada.setEstado(EstadoEntrada.USADA);
        when(entradaRepository.save(entrada)).thenReturn(entrada);

        // Act
        Entrada resultado = entradaService.actualizar(entrada);

        // Assert
        assertThat(resultado.getEstado()).isEqualTo(EstadoEntrada.USADA);
        verify(entradaRepository).save(entrada);
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        // Arrange
        doNothing().when(entradaRepository).deleteById(5L);

        // Act
        entradaService.eliminar(5L);

        // Assert
        verify(entradaRepository).deleteById(5L);
    }
}
