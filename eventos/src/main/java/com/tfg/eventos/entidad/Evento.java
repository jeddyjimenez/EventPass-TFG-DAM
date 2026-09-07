package com.tfg.eventos.entidad;
import jakarta.persistence.*;
import com.tfg.eventos.entidad.enums.EstadoEvento;
import com.tfg.eventos.entidad.enums.TipoEvento;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;

// Clase entidad de la tabla eventos, contiene sus atributos, constructores,
// getters y setters. También sus relaciones.

@Entity
@Table(name = "eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha_inicio;

    @Column(nullable = false)
    private LocalDateTime fecha_fin;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(name = "imagen_url")
    private String imagen_url;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "estado_evento")
    private EstadoEvento estado;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "tipo_evento")
    private TipoEvento tipo;

    @Column(nullable = false)
    private java.math.BigDecimal precio;

    @Column(nullable = false)
    private LocalDateTime creado_en;

    @ManyToOne
    @JoinColumn(name = "id_organizador", nullable = false)
    private Usuario organizador;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private List<Asistente> asistentes;

    @ManyToMany
    @JoinTable(
            name = "evento_validadores",
            joinColumns = @JoinColumn(name = "id_evento"),
            inverseJoinColumns = @JoinColumn(name = "id_validador")
    )
    // Lista de validadores asignados al evento
    private List<Usuario> validadores;

    public Evento() {
    }

    public Evento(String nombre, String descripcion, LocalDateTime fecha_inicio,
                  LocalDateTime fecha_fin, String ubicacion, Integer capacidad, Usuario organizador) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.organizador = organizador;
        this.estado = EstadoEvento.PLANIFICADO;
        this.creado_en = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fecha_inicio;
    }

    public void setFechaInicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDateTime getFechaFin() {
        return fecha_fin;
    }

    public void setFechaFin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getImagenUrl() {
        return imagen_url;
    }

    public void setImagenUrl(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }

    public List<Asistente> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<Asistente> asistentes) {
        this.asistentes = asistentes;
    }

    public List<Usuario> getValidadores() {
        return validadores;
    }

    public void setValidadores(List<Usuario> validadores) {
        this.validadores = validadores;
    }

    public java.math.BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(java.math.BigDecimal precio) {
        this.precio = precio;
    }
}
