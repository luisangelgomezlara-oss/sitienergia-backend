package com.nexuscode.registro.controllers;

import com.nexuscode.registro.dto.SolicitudRequest;
import com.nexuscode.registro.entities.Solicitud;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import com.nexuscode.registro.services.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final UserRepository userRepository;

    public SolicitudController(SolicitudService solicitudService, UserRepository userRepository) {
        this.solicitudService = solicitudService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public Solicitud registrar(@Valid @RequestBody SolicitudRequest request, Principal principal) {
        User usuarioLogueado = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        return solicitudService.registrarSolicitud(usuarioLogueado.getId(), request);
    }

    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public List<Solicitud> obtenerMisSolicitudes(Principal principal) {
        User usuarioLogueado = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        return solicitudService.obtenerPorUsuario(usuarioLogueado.getId());
    }

    // --- SOLO PARA EL ADMIN ---

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Solicitud> obtenerTodas() {
        return solicitudService.obtenerTodas();
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public Solicitud actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return solicitudService.actualizarEstado(id, estado);
    }
}