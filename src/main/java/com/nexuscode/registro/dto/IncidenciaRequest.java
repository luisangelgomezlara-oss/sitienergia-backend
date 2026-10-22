package com.nexuscode.registro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IncidenciaRequest(
        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotBlank(message = "El NIC es obligatorio")
        String nic, // ¡Aquí está el nuevo campo!

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotBlank(message = "La ubicación es obligatoria")
        String ubicacion,

        // Coordenadas opcionales para cuando implementemos Maps
        Double latitud,
        Double longitud
) {}