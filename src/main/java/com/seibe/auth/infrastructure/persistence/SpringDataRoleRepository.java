package com.seibe.auth.infrastructure.persistence;

import com.seibe.auth.domain.entity.ERole;
import com.seibe.auth.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Role
 */
@Repository
public interface SpringDataRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
} 