package com.nexuscode.registro.services;

import com.nexuscode.registro.entities.MantenimientoProgramado;
import com.nexuscode.registro.repositories.MantenimientoProgramadoRepository;
import com.nexuscode.registro.services.MantenimientoProgramadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MantenimientoProgramadoServiceImpl implements MantenimientoProgramadoService {

    @Autowired
    private MantenimientoProgramadoRepository repository;

    @Override
    public List<MantenimientoProgramado> obtenerMantenimientosActivos() {
        return repository.findByEstadoInOrderByFechaInicioAsc(
                Arrays.asList("PROGRAMADO", "EN_CURSO")
        );
    }

    @Override
    public List<MantenimientoProgramado> obtenerTodos() {
        return repository.findAll();
    }

    @Override
    public MantenimientoProgramado crearMantenimiento(MantenimientoProgramado mantenimiento) {
        if (mantenimiento.getEstado() == null || mantenimiento.getEstado().isEmpty()) {
            mantenimiento.setEstado("PROGRAMADO");
        }
        return repository.save(mantenimiento);
    }

    @Override
    public Optional<MantenimientoProgramado> actualizarEstado(Long id, String nuevoEstado) {
        return repository.findById(id).map(mantenimiento -> {
            mantenimiento.setEstado(nuevoEstado.toUpperCase());
            return repository.save(mantenimiento);
        });
    }
}