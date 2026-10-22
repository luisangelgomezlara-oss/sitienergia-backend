package com.nexuscode.registro.services;

import com.nexuscode.registro.controllers.AuthController.AuthResponse;
import com.nexuscode.registro.controllers.AuthController.LoginClienteRequest;
import com.nexuscode.registro.controllers.AuthController.LoginRequest;
import com.nexuscode.registro.controllers.AuthController.RegisterRequest;
import com.nexuscode.registro.controllers.AuthController.UserResponse;
import com.nexuscode.registro.controllers.AuthController.RegisterClienteRequest; // Agregamos este import

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse loginCliente(LoginClienteRequest request);
    UserResponse register(RegisterRequest request);
    UserResponse registerCliente(RegisterClienteRequest request); // <--- AQUÍ VA EL NUEVO MÉTODO
}