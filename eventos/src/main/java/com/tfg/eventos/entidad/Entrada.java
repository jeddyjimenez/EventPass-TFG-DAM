package com.tfg.eventos.entidad;

import jakarta.persistence.*;
import com.tfg.eventos.entidad.enums.EstadoEntrada;
import com.tfg.eventos.entidad.enums.EstadoPago;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.List;

// Clase entidad de la tabla entradas, contiene sus atributos, constructores,
// getters y setters. También sus relaciones.

@Entity
@Table(name = "entradas")
public class Entrada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_token", nullable = false, unique = true)
    // Token único que después se usa para generar y validar el QR
    private String qrToken;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "estado_entrada")
    private EstadoEntrada estado;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "estado_pago")
    private EstadoPago estado_pago;

    @Column(nullable = false)
    private LocalDateTime comprada_en;

    @Column
    private LocalDateTime usada_en;

    @ManyToOne
    @JoinColumn(name = "id_asistente", nullable = false)
    private Asistente asistente;

    @OneToMany(mappedBy = "entrada", cascade = CascadeType.ALL)
    private List<RegistroAcceso> registros;

    public Entrada() {
    }

    public Entrada(String qrToken, EstadoEntrada estado, EstadoPago estado_pago,
                   LocalDateTime comprada_en, Asistente asistente) {
        this.qrToken = qrToken;
        this.estado = estado;
        this.estado_pago = estado_pago;
        this.comprada_en = comprada_en;
        this.asistente = asistente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }

    public EstadoPago getEstadoPago() {
        return estado_pago;
    }

    public void setEstadoPago(EstadoPago estado_pago) {
        this.estado_pago = estado_pago;
    }

    public LocalDateTime getCompradaEn() {
        return comprada_en;
    }

    public void setCompradaEn(LocalDateTime comprada_en) {
        this.comprada_en = comprada_en;
    }

    public LocalDateTime getUsadaEn() {
        return usada_en;
    }

    public void setUsadaEn(LocalDateTime usada_en) {
        this.usada_en = usada_en;
    }

    public Asistente getAsistente() {
        return asistente;
    }

    public void setAsistente(Asistente asistente) {
        this.asistente = asistente;
    }

    public List<RegistroAcceso> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroAcceso> registros) {
        this.registros = registros;
    }
}
