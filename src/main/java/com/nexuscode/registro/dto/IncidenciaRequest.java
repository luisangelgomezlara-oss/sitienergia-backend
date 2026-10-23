package com.nexuscode.registro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IncidenciaRequest(
        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotBlank(message = "El NIC es obligatorio")
        String nic,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        @NotBlank(message = "La ubicación es obligatoria")
        String ubicacion,

        Double latitud,

        Double longitud,

        String imagen
) {}