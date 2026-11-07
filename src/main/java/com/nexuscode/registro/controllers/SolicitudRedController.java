package com.nexuscode.registro.controllers;

import com.nexuscode.registro.entities.SolicitudRed;
import com.nexuscode.registro.entities.EstadoSolicitud;
import com.nexuscode.registro.services.SolicitudRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-red")
@CrossOrigin(origins = "*")
public class SolicitudRedController {

    @Autowired
    private SolicitudRedService service;

    @PostMapping("/radicar")
    public ResponseEntity<SolicitudRed> radicar(@RequestBody SolicitudRed solicitud) {
        return ResponseEntity.ok(service.radicarSolicitud(solicitud));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudRed>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("/cliente/{cedula}")
    public ResponseEntity<List<SolicitudRed>> misSolicitudes(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(service.obtenerPorCedula(cedula));
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<List<SolicitudRed>> misSolicitudesPorCedula(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(service.obtenerPorCedula(cedula));
    }

    @GetMapping("/admin/pendientes")
    public ResponseEntity<List<SolicitudRed>> pendientes() {
        return ResponseEntity.ok(service.obtenerPendientesRevision());
    }

    @GetMapping("/cuadrilla/{id}")
    public ResponseEntity<List<SolicitudRed>> tareasCuadrilla(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obtenerPorCuadrilla(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudRed> cambiarEstado(
            @PathVariable("id") Long id,
            @RequestParam("estado") EstadoSolicitud estado,
            @RequestParam(value = "conceptoTecnico", required = false) String conceptoTecnico) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado, conceptoTecnico));
    }

    // CORREGIDO: Parámetros nombrados explícitamente ("id" y "cuadrillaId")
    @PutMapping("/{id}/asignar-cuadrilla")
    public ResponseEntity<SolicitudRed> asignarCuadrilla(
            @PathVariable("id") Long id,
            @RequestParam("cuadrillaId") Long cuadrillaId) {
        return ResponseEntity.ok(service.asignarCuadrilla(id, cuadrillaId));
    }
}