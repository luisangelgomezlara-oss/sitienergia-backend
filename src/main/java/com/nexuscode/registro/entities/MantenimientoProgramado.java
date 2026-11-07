package com.nexuscode.registro.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimientos_programados")
public class MantenimientoProgramado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sector; // Ejemplo: "Barrio Centro, San Carlos"

    @Column(nullable = false, length = 500)
    private String descripcion; // Ejemplo: "Cambio de redes de media tensión"

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private String estado; // PROGRAMADO, EN_CURSO, FINALIZADO, CANCELADO

    // --- Constructor vacío ---
    public MantenimientoProgramado() {}

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}