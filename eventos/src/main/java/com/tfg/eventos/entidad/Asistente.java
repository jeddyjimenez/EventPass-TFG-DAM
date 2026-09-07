package com.tfg.eventos.entidad;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

// Clase entidad de la tabla asistentes, contiene sus atributos, constructores,
// getters y setters. También sus relaciones.

@Entity
@Table(name = "asistentes")
public class Asistente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime registrado_en;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @OneToMany(mappedBy = "asistente", cascade = CascadeType.ALL)
    private List<Entrada> entradas;

    public Asistente() {
    }

    public Asistente(Usuario usuario, Evento evento) {
        this.usuario = usuario;
        this.evento = evento;
        this.registrado_en = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRegistradoEn() {
        return registrado_en;
    }

    public void setRegistradoEn(LocalDateTime registrado_en) {
        this.registrado_en = registrado_en;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }
}