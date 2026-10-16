package com.nexuscode.registro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SolicitudRequest(
        @NotBlank(message = "El tipo de servicio es obligatorio")
        String tipoServicio,

        @NotNull(message = "La fecha programada es obligatoria")
        LocalDate fechaProgramada
) {}