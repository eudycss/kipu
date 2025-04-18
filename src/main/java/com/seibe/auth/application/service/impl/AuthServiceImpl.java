package com.seibe.auth.application.service.impl;

import com.seibe.auth.domain.entity.ERole;
import com.seibe.auth.domain.entity.Role;
import com.seibe.auth.domain.entity.User;
import com.seibe.auth.domain.repository.RoleRepository;
import com.seibe.auth.domain.repository.UserRepository;
import com.seibe.auth.domain.service.AuthService;
import com.seibe.auth.infrastructure.security.jwt.JwtUtils;
import com.seibe.auth.infrastructure.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Override
    public Optional<User> authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Return the user if authentication succeeds
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            // Authentication failed
            return Optional.empty();
        }
    }

    @Override
    public User registerUser(String username, String email, String password, Set<String> strRoles) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Error: El nombre de usuario ya está en uso!");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: El email ya está en uso!");
        }

        User user = new User(username, email, encoder.encode(password));

        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_DOCENTE)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "rector":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_RECTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRole);
                        break;
                    //case "admin":
                    //    Role modRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    //            .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                    //    roles.add(modRole);
                    //    break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_DOCENTE)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
} 