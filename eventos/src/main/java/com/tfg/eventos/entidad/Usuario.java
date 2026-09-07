package com.tfg.eventos.entidad;
import jakarta.persistence.*;
import com.tfg.eventos.entidad.enums.RolUsuario;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;

// Clase entidad de la tabla usuarios, contiene sus atributos, constructores,
// getters y setters. También sus relaciones.

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "contrasena_cifrada", nullable = false)
    private String contrasena_cifrada;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "rol_usuario")
    private RolUsuario rol;

    @Column(nullable = false)
    private LocalDateTime creado_en;

    @OneToMany(mappedBy = "organizador", cascade = CascadeType.ALL)
    private List<Evento> eventos;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Asistente> asistentes;

    @ManyToMany(mappedBy = "validadores")
    // Eventos donde el usuario participa como validador
    private List<Evento> eventosAsignados;

    public Usuario() {
    }

    public Usuario(String email, String nombre, String contrasena_cifrada, RolUsuario rol) {
        this.email = email;
        this.nombre = nombre;
        this.contrasena_cifrada = contrasena_cifrada;
        this.rol = rol;
        this.creado_en = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenaCifrada() {
        return contrasena_cifrada;
    }

    public void setContrasenaCifrada(String contrasena_cifrada) {
        this.contrasena_cifrada = contrasena_cifrada;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public LocalDateTime getCreadoEn() {
        return creado_en;
    }

    public void setCreadoEn(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public List<Asistente> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<Asistente> asistentes) {
        this.asistentes = asistentes;
    }

    public List<Evento> getEventosAsignados() {
        return eventosAsignados;
    }

    public void setEventosAsignados(List<Evento> eventosAsignados) {
        this.eventosAsignados = eventosAsignados;
    }
}
