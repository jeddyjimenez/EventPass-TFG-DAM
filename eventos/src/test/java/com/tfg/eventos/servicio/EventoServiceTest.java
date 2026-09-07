package com.tfg.eventos.servicio;

import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.repositorio.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;
    private Usuario organizador;

    @BeforeEach
    void setUp() {
        organizador = new Usuario();
        organizador.setId(1L);

        evento = new Evento();
        evento.setId(10L);
        evento.setNombre("Concierto");
        evento.setOrganizador(organizador);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeEventos() {
        // Arrange
        List<Evento> lista = Arrays.asList(evento);
        when(eventoRepository.findAll()).thenReturn(lista);

        // Act
        List<Evento> resultado = eventoService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Concierto");
        verify(eventoRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarEvento() {
        // Arrange
        when(eventoRepository.findById(10L)).thenReturn(Optional.of(evento));

        // Act
        Optional<Evento> resultado = eventoService.obtenerPorId(10L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(10L);
        verify(eventoRepository).findById(10L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Evento> resultado = eventoService.obtenerPorId(99L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorOrganizador_deberiaRetornarEventosDelOrganizador() {
        // Arrange
        List<Evento> lista = Arrays.asList(evento);
        when(eventoRepository.findByOrganizador(organizador)).thenReturn(lista);

        // Act
        List<Evento> resultado = eventoService.obtenerPorOrganizador(organizador);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getOrganizador()).isEqualTo(organizador);
        verify(eventoRepository).findByOrganizador(organizador);
    }

    @Test
    void guardar_deberiaRetornarEventoGuardado() {
        // Arrange
        when(eventoRepository.save(evento)).thenReturn(evento);

        // Act
        Evento resultado = eventoService.guardar(evento);

        // Assert
        assertThat(resultado).isEqualTo(evento);
        verify(eventoRepository).save(evento);
    }

    @Test
    void actualizar_deberiaRetornarEventoActualizado() {
        // Arrange
        evento.setNombre("Concierto Actualizado");
        when(eventoRepository.save(evento)).thenReturn(evento);

        // Act
        Evento resultado = eventoService.actualizar(evento);

        // Assert
        assertThat(resultado.getNombre()).isEqualTo("Concierto Actualizado");
        verify(eventoRepository).save(evento);
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        // Arrange
        doNothing().when(eventoRepository).deleteById(10L);

        // Act
        eventoService.eliminar(10L);

        // Assert
        verify(eventoRepository).deleteById(10L);
    }
}
