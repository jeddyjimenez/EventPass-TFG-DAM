package com.tfg.eventos.servicio;

import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.RegistroAcceso;
import com.tfg.eventos.repositorio.RegistroAccesoRepository;
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
class RegistroAccesoServiceTest {

    @Mock
    private RegistroAccesoRepository registroAccesoRepository;

    @InjectMocks
    private RegistroAccesoService registroAccesoService;

    private RegistroAcceso registro;
    private Entrada entrada;

    @BeforeEach
    void setUp() {
        entrada = new Entrada();
        entrada.setId(5L);

        registro = new RegistroAcceso(entrada, LocalDateTime.of(2026, 4, 28, 20, 0), "Acceso correcto");
        registro.setId(100L);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeRegistros() {
        // Arrange
        when(registroAccesoRepository.findAll()).thenReturn(Arrays.asList(registro));

        // Act
        List<RegistroAcceso> resultado = registroAccesoService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(1);
        verify(registroAccesoRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarRegistro() {
        // Arrange
        when(registroAccesoRepository.findById(100L)).thenReturn(Optional.of(registro));

        // Act
        Optional<RegistroAcceso> resultado = registroAccesoService.obtenerPorId(100L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(100L);
        verify(registroAccesoRepository).findById(100L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(registroAccesoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<RegistroAcceso> resultado = registroAccesoService.obtenerPorId(99L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorEntrada_deberiaRetornarRegistrosDeLaEntrada() {
        // Arrange
        when(registroAccesoRepository.findByEntrada(entrada)).thenReturn(Arrays.asList(registro));

        // Act
        List<RegistroAcceso> resultado = registroAccesoService.obtenerPorEntrada(entrada);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEntrada()).isEqualTo(entrada);
        verify(registroAccesoRepository).findByEntrada(entrada);
    }

    @Test
    void guardar_deberiaRetornarRegistroGuardado() {
        // Arrange
        when(registroAccesoRepository.save(registro)).thenReturn(registro);

        // Act
        RegistroAcceso resultado = registroAccesoService.guardar(registro);

        // Assert
        assertThat(resultado).isEqualTo(registro);
        verify(registroAccesoRepository).save(registro);
    }

    @Test
    void actualizar_deberiaRetornarRegistroActualizado() {
        // Arrange
        registro.setObservaciones("Acceso denegado");
        when(registroAccesoRepository.save(registro)).thenReturn(registro);

        // Act
        RegistroAcceso resultado = registroAccesoService.actualizar(registro);

        // Assert
        assertThat(resultado.getObservaciones()).isEqualTo("Acceso denegado");
        verify(registroAccesoRepository).save(registro);
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        // Arrange
        doNothing().when(registroAccesoRepository).deleteById(100L);

        // Act
        registroAccesoService.eliminar(100L);

        // Assert
        verify(registroAccesoRepository).deleteById(100L);
    }
}
