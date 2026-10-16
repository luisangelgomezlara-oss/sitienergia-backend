package com.nexuscode.registro.services;

import com.nexuscode.registro.dto.SolicitudRequest;
import com.nexuscode.registro.entities.Solicitud;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.SolicitudRepository;
import com.nexuscode.registro.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // ¡No olvides esta anotación!
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final UserRepository userRepository;

    // Inyección de dependencias por constructor
    public SolicitudServiceImpl(SolicitudRepository solicitudRepository, UserRepository userRepository) {
        this.solicitudRepository = solicitudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Solicitud registrarSolicitud(Long usuarioId, SolicitudRequest request) {
        User cliente = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        Solicitud nueva = new Solicitud();
        nueva.setUsuario(cliente);
        nueva.setTipoServicio(request.tipoServicio());
        nueva.setFechaProgramada(request.fechaProgramada());

        // El estado y la fecha de creación se asignan solos por el @PrePersist en la entidad

        return solicitudRepository.save(nueva);
    }

    @Override
    public List<Solicitud> obtenerPorUsuario(Long usuarioId) {
        return solicitudRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Solicitud> obtenerTodas() {
        return solicitudRepository.findAll();
    }

    @Override
    public Solicitud actualizarEstado(Long id, String estado) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));

        solicitud.setEstado(estado);
        return solicitudRepository.save(solicitud);
    }
}