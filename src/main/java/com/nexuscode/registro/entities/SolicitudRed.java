package com.nexuscode.registro.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_red")
public class SolicitudRed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cedula")
    private String numeroCedula;

    @Column(name = "numero_nic")
    private String numeroNic;

    @Column(name = "tipo_intervencion")
    private String tipoIntervencion;

    @Column(name = "area")
    private String area;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "motivo")
    private String motivo;

    // ANOTACIÓN CLAVE: Le indica a Hibernate que serialice la cadena como JSON nativo de PostgreSQL
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "detalles_especificos", columnDefinition = "jsonb")
    private String detallesEspecificos;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoSolicitud estado;

    @Column(name = "concepto_tecnico")
    private String conceptoTecnico;

    @Column(name = "cuadrilla_id")
    private Long cuadrillaId;

    public SolicitudRed() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroCedula() { return numeroCedula; }
    public void setNumeroCedula(String numeroCedula) { this.numeroCedula = numeroCedula; }

    public String getNumeroNic() { return numeroNic; }
    public void setNumeroNic(String numeroNic) { this.numeroNic = numeroNic; }

    public String getTipoIntervencion() { return tipoIntervencion; }
    public void setTipoIntervencion(String tipoIntervencion) { this.tipoIntervencion = tipoIntervencion; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDetallesEspecificos() { return detallesEspecificos; }
    public void setDetallesEspecificos(String detallesEspecificos) { this.detallesEspecificos = detallesEspecificos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }

    public String getConceptoTecnico() { return conceptoTecnico; }
    public void setConceptoTecnico(String conceptoTecnico) { this.conceptoTecnico = conceptoTecnico; }

    public Long getCuadrillaId() { return cuadrillaId; }
    public void setCuadrillaId(Long cuadrillaId) { this.cuadrillaId = cuadrillaId; }
}