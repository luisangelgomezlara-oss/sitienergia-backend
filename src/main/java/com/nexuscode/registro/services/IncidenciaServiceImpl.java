package com.nexuscode.registro.services;

import com.nexuscode.registro.dto.IncidenciaRequest;
import com.nexuscode.registro.entities.Incidencia;
import com.nexuscode.registro.entities.IncidentCategory;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.IncidenciaRepository;
import com.nexuscode.registro.repositories.IncidentCategoryRepository;
import com.nexuscode.registro.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UserRepository userRepository;
    private final IncidentCategoryRepository categoryRepository;

    public IncidenciaServiceImpl(IncidenciaRepository incidenciaRepository,
                                 UserRepository userRepository,
                                 IncidentCategoryRepository categoryRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Incidencia registrarIncidencia(Long usuarioId, IncidenciaRequest request) {

        // 1. Buscamos al usuario que está haciendo el reporte
        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 2. Buscamos la categoría seleccionada en la app
        IncidentCategory categoria = categoryRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        // 3. Armamos la incidencia con todos los datos
        Incidencia incidencia = new Incidencia();
        incidencia.setUsuario(usuario);
        incidencia.setCategoria(categoria);
        incidencia.setNic(request.nic());
        incidencia.setDescripcion(request.descripcion());
        incidencia.setUbicacion(request.ubicacion());
        incidencia.setLatitud(request.latitud());
        incidencia.setLongitud(request.longitud());
        incidencia.setImagen(request.imagen());
        incidencia.setEstado("PENDIENTE"); // Estado por defecto al crear

        // --- 4. ENRUTAMIENTO INTELIGENTE DIRECTO POR EL ÁREA DE LA CATEGORÍA ---
        String area = categoria.getArea() != null ? categoria.getArea().toUpperCase().trim() : "";

        if (area.contains("OBRA") || area.contains("CIVIL")) {
            incidencia.setCuadrillaAsignada("Cuadrilla Pesada - Obras Civiles (Grúas)");
        } else if (area.contains("RED") || area.contains("ELECTRICA")) {
            incidencia.setCuadrillaAsignada("Cuadrilla Operativa - Redes y Transformadores");
        } else if (area.contains("COMERCIAL")) {
            incidencia.setCuadrillaAsignada("Cuadrilla Técnica - Control y Medida");
        } else {
            incidencia.setCuadrillaAsignada("Cuadrilla General de Emergencias");
        }

        // 5. Guardamos en PostgreSQL local con su cuadrilla asignada correctamente
        return incidenciaRepository.save(incidencia);
    }

    @Override
    public List<Incidencia> obtenerPorUsuario(Long usuarioId) {
        return incidenciaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Incidencia> obtenerTodas() {
        return incidenciaRepository.findAll();
    }

    @Override
    public Incidencia asignarTecnico(Long incidenciaId, Long tecnicoId) {
        Incidencia incidencia = incidenciaRepository.findById(incidenciaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidencia no encontrada"));

        User tecnico = userRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Técnico no encontrado"));

        incidencia.setTecnicoAsignado(tecnico);
        incidencia.setEstado("ASIGNADA");
        return incidenciaRepository.save(incidencia);
    }

    @Override
    public List<Incidencia> obtenerPorTecnico(Long tecnicoId) {
        return incidenciaRepository.findByTecnicoAsignadoId(tecnicoId);
    }

    @Override
    public Incidencia actualizarEstado(Long incidenciaId, String nuevoEstado) {
        Incidencia incidencia = incidenciaRepository.findById(incidenciaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incidencia no encontrada"));

        incidencia.setEstado(nuevoEstado);
        return incidenciaRepository.save(incidencia);
    }
}