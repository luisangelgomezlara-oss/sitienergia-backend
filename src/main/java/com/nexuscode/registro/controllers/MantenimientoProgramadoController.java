package com.nexuscode.registro.controllers;

import com.nexuscode.registro.entities.MantenimientoProgramado;
import com.nexuscode.registro.services.MantenimientoProgramadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoProgramadoController {

    @Autowired
    private MantenimientoProgramadoService service;

    // Cualquier usuario logueado puede ver los activos
    @GetMapping("/activos")
    public ResponseEntity<List<MantenimientoProgramado>> getMantenimientosActivos() {
        return ResponseEntity.ok(service.obtenerMantenimientosActivos());
    }

    // Solo ADMIN puede ver todo el historial
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')") // <--- SOLUCIÓN ROL
    public ResponseEntity<List<MantenimientoProgramado>> getAllMantenimientos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // Solo ADMIN puede crear
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')") // <--- SOLUCIÓN ROL
    public ResponseEntity<MantenimientoProgramado> crearMantenimiento(@RequestBody MantenimientoProgramado mantenimiento) {
        return ResponseEntity.ok(service.crearMantenimiento(mantenimiento));
    }

    // Solo ADMIN puede actualizar estado
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROLE_ADMIN')") // <--- SOLUCIÓN ROL
    public ResponseEntity<MantenimientoProgramado> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return service.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}