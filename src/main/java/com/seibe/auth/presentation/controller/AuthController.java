package com.seibe.auth.presentation.controller;

import com.seibe.auth.application.dto.request.LoginRequest;
import com.seibe.auth.application.dto.request.SignupRequest;
import com.seibe.auth.application.dto.response.JwtResponse;
import com.seibe.auth.application.dto.response.MessageResponse;
import com.seibe.auth.domain.entity.User;
import com.seibe.auth.domain.service.AuthService;
import com.seibe.auth.infrastructure.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador para operaciones de autenticación
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(new JwtResponse(
                jwt, 
                user.getId(), 
                user.getUsername(),
                user.getEmail(),
                roles));
        }
        
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Credenciales inválidas!"));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (authService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: El nombre de usuario ya está en uso!"));
        }
        
        if (authService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: El email ya está en uso!"));
        }
        
        // Crear nuevo usuario
        User user = authService.registerUser(
            signUpRequest.getUsername(), 
            signUpRequest.getEmail(),
            signUpRequest.getPassword(),
            signUpRequest.getRoles()
        );
        
        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente!"));
    }
} 