package com.nexuscode.registro.repositories;

import com.nexuscode.registro.entities.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {
    
   
    List<Incidencia> findByUsuarioId(Long usuarioId);

    List<Incidencia> findByTecnicoAsignadoId(Long tecnicoId);
    
    List<Incidencia> findByEstado(String estado);
}