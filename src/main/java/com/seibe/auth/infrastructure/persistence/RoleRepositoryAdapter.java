package com.seibe.auth.infrastructure.persistence;

import com.seibe.auth.domain.entity.ERole;
import com.seibe.auth.domain.entity.Role;
import com.seibe.auth.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador que implementa la interfaz de repositorio del dominio para roles
 */
@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
    
    private final SpringDataRoleRepository springDataRoleRepository;
    
    @Override
    public Optional<Role> findByName(ERole name) {
        return springDataRoleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return springDataRoleRepository.save(role);
    }
} 