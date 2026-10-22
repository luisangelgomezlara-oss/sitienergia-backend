package com.nexuscode.registro.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginClienteRequest(
        @NotBlank(message = "El NIC es obligatorio")
        String nic,

        @NotBlank(message = "La cédula es obligatoria")
        String cedula
) {}