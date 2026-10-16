package com.nexuscode.registro.controllers;

import com.nexuscode.registro.dto.IncidenciaRequest;
import com.nexuscode.registro.entities.Incidencia;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import com.nexuscode.registro.services.IncidenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;
    private final UserRepository userRepository; // Añadido para buscar el usuario por el token

    public IncidenciaController(IncidenciaService incidenciaService, UserRepository userRepository) {
        this.incidenciaService = incidenciaService;
        this.userRepository = userRepository;
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL CLIENTE
    // --------------------------------------------------------

    // Eliminamos el {usuarioId} de la ruta. Spring Boot lo saca del "Principal"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public Incidencia registrar(@Valid @RequestBody IncidenciaRequest request, Principal principal) {
        User usuarioLogueado = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        return incidenciaService.registrarIncidencia(usuarioLogueado.getId(), request);
    }

    @GetMapping("/mis-reportes")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public List<Incidencia> obtenerPorCliente(Principal principal) {
        User usuarioLogueado = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        return incidenciaService.obtenerPorUsuario(usuarioLogueado.getId());
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL ADMINISTRADOR
    // --------------------------------------------------------

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Incidencia> obtenerTodas() {
        return incidenciaService.obtenerTodas();
    }

    @PutMapping("/{id}/asignar/{tecnicoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Incidencia asignarTecnico(@PathVariable Long id, @PathVariable Long tecnicoId) {
        return incidenciaService.asignarTecnico(id, tecnicoId);
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL TÉCNICO
    // --------------------------------------------------------

    @GetMapping("/mis-asignaciones")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public List<Incidencia> obtenerPorTecnico(Principal principal) {
        // También aseguramos al técnico con el token para que no vea los trabajos de otros técnicos
        User tecnicoLogueado = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));

        return incidenciaService.obtenerPorTecnico(tecnicoLogueado.getId());
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public Incidencia actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return incidenciaService.actualizarEstado(id, estado);
    }
}