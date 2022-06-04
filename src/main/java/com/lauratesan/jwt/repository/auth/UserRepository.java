package com.lauratesan.jwt.repository.auth;

import com.lauratesan.jwt.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByResetPasswordToken(String token);

    boolean existsByEmail(String email);
}
