package com.nexuscode.registro.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nexuscode.registro.controllers.AuthController.UserResponse;
import com.nexuscode.registro.controllers.UserController.UserRequest;
import com.nexuscode.registro.controllers.UserController.UpdateUserRequest;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;

import java.util.Collections;
import java.util.List;

@Service // ¡Muy importante esta anotación para Spring!
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    // Este es el método clave que Spring Security necesitaba para autenticar contra la BD
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = users.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActivo(), // Valida si está activo
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRol()))
        );
    }

    @Override
    public List<UserResponse> obtenerTodos() {
        return users.findAll().stream().map(UserResponse::from).toList();
    }

    @Override
    public UserResponse crearUsuario(UserRequest request) {
        if (users.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya esta registrado");
        }

        User nuevoUsuario = new User(
                request.nombre(),
                request.email(),
                encoder.encode(request.password()),
                request.rol().name(),
                request.telefono()
        );
        return UserResponse.from(users.save(nuevoUsuario));
    }

    @Override
    public UserResponse actualizarUsuario(Long id, UpdateUserRequest request) {
        User user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        user.setNombre(request.nombre());
        user.setRol(request.rol().name());
        user.setTelefono(request.telefono());
        user.setActivo(request.activo());

        return UserResponse.from(users.save(user));
    }

    @Override
    public void desactivarUsuario(Long id) {
        User user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setActivo(false);
        users.save(user);
    }
}