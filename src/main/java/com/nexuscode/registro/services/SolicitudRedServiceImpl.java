package com.nexuscode.registro.services;

import com.nexuscode.registro.entities.SolicitudRed;
import com.nexuscode.registro.entities.EstadoSolicitud;
import com.nexuscode.registro.repositories.SolicitudRedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudRedServiceImpl implements SolicitudRedService {

    @Autowired
    private SolicitudRedRepository repository;

    @Override
    public SolicitudRed radicarSolicitud(SolicitudRed solicitud) {
        solicitud.setEstado(EstadoSolicitud.RADICADA);
        solicitud.setFechaCreacion(LocalDateTime.now());
        return repository.save(solicitud);
    }

    @Override
    public List<SolicitudRed> obtenerPorCedula(String cedula) {
        return repository.findByNumeroCedula(cedula);
    }

    @Override
    public List<SolicitudRed> obtenerPendientesRevision() {
        return repository.findByEstado(EstadoSolicitud.RADICADA);
    }

    @Override
    public List<SolicitudRed> obtenerPorCuadrilla(Long cuadrillaId) {
        return repository.findByCuadrillaId(cuadrillaId);
    }

    @Override
    public List<SolicitudRed> obtenerTodas() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public SolicitudRed actualizarEstado(Long id, EstadoSolicitud nuevoEstado, String conceptoTecnico) {
        SolicitudRed solicitud = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(nuevoEstado);
        if (conceptoTecnico != null && !conceptoTecnico.isEmpty()) {
            solicitud.setConceptoTecnico(conceptoTecnico);
        }
        solicitud.setFechaActualizacion(LocalDateTime.now());
        return repository.save(solicitud);
    }

    @Override
    @Transactional
    public SolicitudRed asignarCuadrilla(Long solicitudId, Long cuadrillaId) {
        // Ejecuta la actualización directa en BD
        int actualizados = repository.asignarCuadrillaDirecto(
                solicitudId,
                cuadrillaId,
                EstadoSolicitud.EN_EJECUCION,
                LocalDateTime.now()
        );

        if (actualizados == 0) {
            throw new RuntimeException("No se encontró la solicitud con ID: " + solicitudId);
        }

        // Retorna la solicitud actualizada
        return repository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Error al recuperar la solicitud asignada"));
    }
}