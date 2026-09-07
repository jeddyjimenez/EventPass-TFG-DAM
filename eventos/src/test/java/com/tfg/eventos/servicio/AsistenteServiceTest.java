package com.tfg.eventos.servicio;

import com.tfg.eventos.entidad.Asistente;
import com.tfg.eventos.entidad.Evento;
import com.tfg.eventos.entidad.Usuario;
import com.tfg.eventos.repositorio.AsistenteRepository;
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
class AsistenteServiceTest {

    @Mock
    private AsistenteRepository asistenteRepository;

    @InjectMocks
    private AsistenteService asistenteService;

    private Asistente asistente;
    private Usuario usuario;
    private Evento evento;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        evento = new Evento();
        evento.setId(10L);

        asistente = new Asistente(usuario, evento);
        asistente.setId(20L);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeAsistentes() {
        // Arrange
        when(asistenteRepository.findAll()).thenReturn(Arrays.asList(asistente));

        // Act
        List<Asistente> resultado = asistenteService.obtenerTodos();

        // Assert
        assertThat(resultado).hasSize(1);
        verify(asistenteRepository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarAsistente() {
        // Arrange
        when(asistenteRepository.findById(20L)).thenReturn(Optional.of(asistente));

        // Act
        Optional<Asistente> resultado = asistenteService.obtenerPorId(20L);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(20L);
        verify(asistenteRepository).findById(20L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(asistenteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Asistente> resultado = asistenteService.obtenerPorId(99L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void obtenerPorEvento_deberiaRetornarAsistentesDelEvento() {
        // Arrange
        when(asistenteRepository.findByEvento(evento)).thenReturn(Arrays.asList(asistente));

        // Act
        List<Asistente> resultado = asistenteService.obtenerPorEvento(evento);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEvento()).isEqualTo(evento);
        verify(asistenteRepository).findByEvento(evento);
    }

    @Test
    void obtenerPorUsuarioYEvento_cuandoExiste_deberiaRetornarAsistente() {
        // Arrange
        when(asistenteRepository.findByUsuarioAndEvento(usuario, evento))
                .thenReturn(Optional.of(asistente));

        // Act
        Optional<Asistente> resultado = asistenteService.obtenerPorUsuarioYEvento(usuario, evento);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getUsuario()).isEqualTo(usuario);
        assertThat(resultado.get().getEvento()).isEqualTo(evento);
        verify(asistenteRepository).findByUsuarioAndEvento(usuario, evento);
    }

    @Test
    void obtenerPorUsuarioYEvento_cuandoNoExiste_deberiaRetornarVacio() {
        // Arrange
        when(asistenteRepository.findByUsuarioAndEvento(usuario, evento))
                .thenReturn(Optional.empty());

        // Act
        Optional<Asistente> resultado = asistenteService.obtenerPorUsuarioYEvento(usuario, evento);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void guardar_deberiaRetornarAsistenteGuardado() {
        // Arrange
        when(asistenteRepository.save(asistente)).thenReturn(asistente);

        // Act
        Asistente resultado = asistenteService.guardar(asistente);

        // Assert
        assertThat(resultado).isEqualTo(asistente);
        verify(asistenteRepository).save(asistente);
    }

    @Test
    void actualizar_deberiaRetornarAsistenteActualizado() {
        // Arrange
        when(asistenteRepository.save(asistente)).thenReturn(asistente);

        // Act
        Asistente resultado = asistenteService.actualizar(asistente);

        // Assert
        assertThat(resultado).isEqualTo(asistente);
        verify(asistenteRepository).save(asistente);
    }

    @Test
    void eliminar_deberiaLlamarDeleteById() {
        // Arrange
        doNothing().when(asistenteRepository).deleteById(20L);

        // Act
        asistenteService.eliminar(20L);

        // Assert
        verify(asistenteRepository).deleteById(20L);
    }
}
