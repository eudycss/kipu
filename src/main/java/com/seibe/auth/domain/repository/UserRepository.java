package com.seibe.auth.domain.repository;

import com.seibe.auth.domain.entity.User;
import java.util.Optional;

/**
 * Repositorio para operaciones con la entidad User
 */
public interface UserRepository {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);
} 