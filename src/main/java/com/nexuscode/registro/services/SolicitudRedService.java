package com.nexuscode.registro.services;

import com.nexuscode.registro.entities.SolicitudRed;
import com.nexuscode.registro.entities.EstadoSolicitud;
import java.util.List;

public interface SolicitudRedService {
    SolicitudRed radicarSolicitud(SolicitudRed solicitud);
    List<SolicitudRed> obtenerPorCedula(String cedula);
    List<SolicitudRed> obtenerPendientesRevision();
    List<SolicitudRed> obtenerPorCuadrilla(Long cuadrillaId);
    List<SolicitudRed> obtenerTodas();
    SolicitudRed actualizarEstado(Long id, EstadoSolicitud nuevoEstado, String conceptoTecnico);
    SolicitudRed asignarCuadrilla(Long solicitudId, Long cuadrillaId);
}