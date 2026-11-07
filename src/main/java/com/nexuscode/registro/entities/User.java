package com.nexuscode.registro.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String rol = "CLIENTE"; // "ADMIN", "TECNICO", "CLIENTE"

    private String telefono;

    @Column(name = "nic", unique = true, nullable = true)
    private String nic;

    @Column(name = "cedula", nullable = true)
    private String cedula;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "especialidad", nullable = true)
    private String especialidad; // Campo para técnicos: REDES ELECTRICAS, OBRAS CIVILES, COMERCIAL

    // --- CONSTRUCTORES ---
    public User() {}

    // Constructor que usa tu UserServiceImpl actualmente
    public User(String nombre, String email, String password, String rol, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.telefono = telefono;
        this.activo = true;
    }

    // Constructor completo por si necesitas instanciarlo con especialidad
    public User(String nombre, String email, String password, String rol, String telefono, String especialidad) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.telefono = telefono;
        this.activo = true;
        this.especialidad = especialidad;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPasswordHash() { return password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public Boolean getActivo() { return activo; }
    public Boolean isActivo() { return activo != null && activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}