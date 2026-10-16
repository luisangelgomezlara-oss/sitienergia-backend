package com.nexuscode.registro.services;

import com.nexuscode.registro.dto.SolicitudRequest;
import com.nexuscode.registro.entities.Solicitud;
import java.util.List;

public interface SolicitudService {
    // Para el Cliente
    Solicitud registrarSolicitud(Long usuarioId, SolicitudRequest request);
    List<Solicitud> obtenerPorUsuario(Long usuarioId);

    // Para el Administrador
    List<Solicitud> obtenerTodas();
    Solicitud actualizarEstado(Long id, String estado);
}