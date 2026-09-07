package com.tfg.eventos.entidad;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// Clase entidad de la tabla registros_acceso, contiene sus atributos, constructores,
// getters y setters. También sus relaciones.

@Entity
@Table(name = "registros_acceso")
public class RegistroAcceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha_acceso;

    @Column
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_entrada", nullable = false)
    private Entrada entrada;

    public RegistroAcceso() {
    }

    public RegistroAcceso(Entrada entrada, LocalDateTime fecha_acceso, String observaciones) {
        this.entrada = entrada;
        this.fecha_acceso = fecha_acceso;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaAcceso() {
        return fecha_acceso;
    }

    public void setFechaAcceso(LocalDateTime fecha_acceso) {
        this.fecha_acceso = fecha_acceso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }
}