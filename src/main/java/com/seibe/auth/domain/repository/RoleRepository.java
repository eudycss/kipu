package com.seibe.auth.domain.repository;

import com.seibe.auth.domain.entity.ERole;
import com.seibe.auth.domain.entity.Role;
import java.util.Optional;

/**
 * Repositorio para operaciones con la entidad Role
 */
public interface RoleRepository {
    Optional<Role> findByName(ERole name);
    Role save(Role role);
} 