package com.nexuscode.registro.controllers;

import com.nexuscode.registro.dto.IncidenciaRequest;
import com.nexuscode.registro.entities.Incidencia;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import com.nexuscode.registro.services.IncidenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;
    private final UserRepository userRepository;

    public IncidenciaController(IncidenciaService incidenciaService, UserRepository userRepository) {
        this.incidenciaService = incidenciaService;
        this.userRepository = userRepository;
    }

    // --- MÉTODO AUXILIAR PARA EVITAR EL ERROR 500 DE JACKSON JSON ---
    private Map<String, Object> mapToDTO(Incidencia inc) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", inc.getId());
        dto.put("descripcion", inc.getDescripcion());
        dto.put("estado", inc.getEstado());
        dto.put("fechaCreacion", inc.getFechaCreacion().toString());
        // Se extrae la categoría de forma segura
        dto.put("categoria", inc.getCategoria() != null ? inc.getCategoria().getNombre() : "Sin categoría");
        return dto;
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL CLIENTE
    // --------------------------------------------------------

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @Transactional
    public Map<String, Object> registrar(@Valid @RequestBody IncidenciaRequest request, Principal principal) {
        User usuarioLogueado = userRepository.findByNic(principal.getName())
                .orElseGet(() -> userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado")));

        Incidencia nuevaIncidencia = incidenciaService.registrarIncidencia(usuarioLogueado.getId(), request);
        return mapToDTO(nuevaIncidencia);
    }

    @GetMapping("/mis-reportes")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerPorCliente(Principal principal) {
        User usuarioLogueado = userRepository.findByNic(principal.getName())
                .orElseGet(() -> userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado")));

        return incidenciaService.obtenerPorUsuario(usuarioLogueado.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL ADMINISTRADOR
    // --------------------------------------------------------

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerTodas() {
        return incidenciaService.obtenerTodas()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/asignar/{tecnicoId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Map<String, Object> asignarTecnico(@PathVariable Long id, @PathVariable Long tecnicoId) {
        return mapToDTO(incidenciaService.asignarTecnico(id, tecnicoId));
    }

    // --------------------------------------------------------
    // ENDPOINTS PARA EL TÉCNICO
    // --------------------------------------------------------

    @GetMapping("/mis-asignaciones")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerPorTecnico(Principal principal) {
        User tecnicoLogueado = userRepository.findByEmail(principal.getName())
                .orElseGet(() -> userRepository.findByNic(principal.getName())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado")));

        return incidenciaService.obtenerPorTecnico(tecnicoLogueado.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @Transactional
    public Map<String, Object> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return mapToDTO(incidenciaService.actualizarEstado(id, estado));
    }
}