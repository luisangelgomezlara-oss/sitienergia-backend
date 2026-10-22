package com.nexuscode.registro.services;

import com.nexuscode.registro.controllers.AuthController.AuthResponse;
import com.nexuscode.registro.controllers.AuthController.LoginClienteRequest;
import com.nexuscode.registro.controllers.AuthController.LoginRequest;
import com.nexuscode.registro.controllers.AuthController.RegisterRequest;
import com.nexuscode.registro.controllers.AuthController.UserResponse;
import com.nexuscode.registro.controllers.AuthController.RegisterClienteRequest; // Nuevo import
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import com.nexuscode.registro.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthServiceImpl(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = users.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));

        if (!user.getActivo() || !encoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        return new AuthResponse(
                jwt.createToken(user.getEmail(), user.getRol()),
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRol()
        );
    }

    @Override
    public AuthResponse loginCliente(LoginClienteRequest request) {
        User user = users.findByNicAndCedula(request.nic(), request.cedula())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "NIC o número de cédula incorrectos"));

        if (!user.getActivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario inactivo");
        }

        String sujetoJwt = (user.getEmail() != null && !user.getEmail().isBlank())
                ? user.getEmail()
                : user.getNic();

        String token = jwt.createToken(sujetoJwt, user.getRol());

        return new AuthResponse(
                token,
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRol()
        );
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }

        User newUser = new User(
                request.nombre(),
                request.email(),
                encoder.encode(request.password()),
                "CLIENTE",
                request.telefono()
        );
        return UserResponse.from(users.save(newUser));
    }

    // --- NUEVO MÉTODO PARA REGISTRAR CLIENTES CON NIC Y CÉDULA ---
    @Override
    public UserResponse registerCliente(RegisterClienteRequest request) {
        // Validamos que no metan un cliente duplicado
        if (users.existsByNic(request.nic()) || users.existsByCedula(request.cedula())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El NIC o la Cédula ya están registrados");
        }

        User newUser = new User();
        newUser.setNombre(request.nombre());
        newUser.setNic(request.nic());
        newUser.setCedula(request.cedula());
        newUser.setTelefono(request.telefono());
        newUser.setRol("CLIENTE");
        newUser.setActivo(true);
        // El email y password quedan nulos por defecto en la base de datos

        return UserResponse.from(users.save(newUser));
    }
}