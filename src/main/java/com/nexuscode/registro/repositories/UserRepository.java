package com.nexuscode.registro.repositories;

import com.nexuscode.registro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // ESTA ES LA LÍNEA QUE TE PIDE EL ERROR DE LA LÍNEA 76
    boolean existsByEmail(String email);
    boolean existsByNic(String nic);
    boolean existsByCedula(String cedula);

    Optional<User> findByNicAndCedula(String nic, String cedula);
    Optional<User> findByNic(String nic);
}