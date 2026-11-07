package com.nexuscode.registro.repositories;

import com.nexuscode.registro.entities.SolicitudRed;
import com.nexuscode.registro.entities.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudRedRepository extends JpaRepository<SolicitudRed, Long> {

    List<SolicitudRed> findByNumeroCedula(String numeroCedula);
    List<SolicitudRed> findByEstado(EstadoSolicitud estado);
    List<SolicitudRed> findByCuadrillaId(Long cuadrillaId);

    // Consulta directa para actualizar solo Cuadrilla y Estado sin tocar el JSON
    @Modifying
    @Query("UPDATE SolicitudRed s SET s.cuadrillaId = :cuadrillaId, s.estado = :estado, s.fechaActualizacion = :fechaActualizacion WHERE s.id = :id")
    int asignarCuadrillaDirecto(
            @Param("id") Long id,
            @Param("cuadrillaId") Long cuadrillaId,
            @Param("estado") EstadoSolicitud estado,
            @Param("fechaActualizacion") LocalDateTime fechaActualizacion
    );
}