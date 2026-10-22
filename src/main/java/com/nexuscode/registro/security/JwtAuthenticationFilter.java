package com.nexuscode.registro.security;

import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository users;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository users) {
        this.jwtService = jwtService;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            try {
                // El "subject" del token ahora puede ser un Email (Admin/Técnico) o un NIC (Cliente)
                String subject = jwtService.extractEmail(header.substring(7));

                // Buscamos primero por email; si no lo encuentra, buscamos por NIC
                users.findByEmail(subject)
                        .or(() -> users.findByNic(subject))
                        .filter(user -> Boolean.TRUE.equals(user.getActivo())) // CORREGIDO: getActivo()
                        .ifPresent(user -> {
                            var auth = new UsernamePasswordAuthenticationToken(
                                    subject,
                                    null,
                                    // CORREGIDO: Quitamos el .name()
                                    java.util.List.of(new SimpleGrantedAuthority("ROLE_" + user.getRol()))
                            );
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        });
            } catch (RuntimeException ignored) {
                /* Token inválido: Security rechazará la petición protegida. */
            }
        }
        chain.doFilter(request, response);
    }
}