package com.seibe.auth.infrastructure.persistence;

import com.seibe.auth.domain.entity.User;
import com.seibe.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador que implementa la interfaz de repositorio del dominio para usuarios
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    
    private final SpringDataUserRepository springDataUserRepository;
    
    @Override
    public Optional<User> findByUsername(String username) {
        return springDataUserRepository.findByUsername(username);
    }
    
    @Override
    public Boolean existsByUsername(String username) {
        return springDataUserRepository.existsByUsername(username);
    }
    
    @Override
    public Boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }
    
    @Override
    public User save(User user) {
        return springDataUserRepository.save(user);
    }
} 