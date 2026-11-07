package com.nexuscode.registro.controllers;

import com.nexuscode.registro.services.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/login-cliente")
    public AuthResponse loginCliente(@Valid @RequestBody LoginClienteRequest request) {
        return authService.loginCliente(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/register-cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerCliente(@Valid @RequestBody RegisterClienteRequest request) {
        return authService.registerCliente(request);
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record LoginClienteRequest(@NotBlank String nic, @NotBlank String cedula) {}
    public record RegisterRequest(@NotBlank String nombre, @Email @NotBlank String email, @NotBlank String password, String telefono) {}
    public record RegisterClienteRequest(@NotBlank String nombre, @NotBlank String nic, @NotBlank String cedula, String telefono) {}

    public record AuthResponse(String token, Long id, String nombre, String email, String rol) {}

    // Incluimos la "cedula" aquí en la respuesta final
    public record UserResponse(Long id, String nombre, String email, String rol, String telefono, String cedula, boolean activo, String especialidad) {
        public static UserResponse from(com.nexuscode.registro.entities.User user) {
            return new UserResponse(
                    user.getId(),
                    user.getNombre(),
                    user.getEmail(),
                    user.getRol(),
                    user.getTelefono(),
                    user.getCedula(), // <-- La enviamos al frontend
                    user.getActivo(),
                    user.getEspecialidad()
            );
        }
    }
}