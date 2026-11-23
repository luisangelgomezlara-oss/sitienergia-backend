package com.nexuscode.registro.controllers;

import com.nexuscode.registro.entities.EstadoSolicitud;
import com.nexuscode.registro.entities.SolicitudRed;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import com.nexuscode.registro.services.SolicitudRedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-red")
@CrossOrigin(origins = "*")
public class SolicitudRedController {

    @Autowired
    private SolicitudRedService service;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/mis-asignaciones")
    public ResponseEntity<List<SolicitudRed>> misAsignacionesTecnico(Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        User tecnicoLogueado = userRepository.findByEmail(principal.getName())
                .orElseGet(() -> userRepository.findByNic(principal.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Técnico no encontrado")));

        return ResponseEntity.ok(service.obtenerPorCuadrilla(tecnicoLogueado.getId()));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudRed> cambiarEstado(
            @PathVariable("id") Long id,
            @RequestParam("estado") EstadoSolicitud estado,
            @RequestParam(value = "conceptoTecnico", required = false) String conceptoTecnico) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado, conceptoTecnico));
    }

    @PutMapping("/{id}/asignar-cuadrilla")
    public ResponseEntity<SolicitudRed> asignarCuadrilla(
            @PathVariable("id") Long id,
            @RequestParam("cuadrillaId") Long cuadrillaId) {
        return ResponseEntity.ok(service.asignarCuadrilla(id, cuadrillaId));
    }
}