package com.nexuscode.registro; // Asegúrate de que este paquete coincida con el tuyo

import com.nexuscode.registro.entities.IncidentCategory;
import com.nexuscode.registro.entities.User;
import com.nexuscode.registro.repositories.IncidentCategoryRepository;
import com.nexuscode.registro.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seed(UserRepository users,
                                  IncidentCategoryRepository categories,
                                  PasswordEncoder encoder) {
        return args -> {
            // Creamos el administrador por defecto si no existe
            if (!users.existsByEmail("admin@sitienergia.com")) {
                User admin = new User();
                admin.setEmail("admin@sitienergia.com");
                admin.setNombre("Administrador SitiEnergia");

                // CORREGIDO: Usamos setPassword() en lugar de setPasswordHash()
                admin.setPassword(encoder.encode("admin123")); // Cambia "admin123" por tu variable adminPassword si la tienes definida

                // CORREGIDO: Pasamos el rol como un texto (String) en lugar del Enum Role.ADMIN
                admin.setRol("ADMIN");

                admin.setActivo(true);
                users.save(admin);
            }

            // Aquí puedes dejar la lógica que tenías para crear las categorías de incidentes
            // si es que las estabas inicializando en este archivo.
        };
    }
}