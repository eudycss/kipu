package com.seibe.auth.domain.service;

import com.seibe.auth.domain.entity.User;

import java.util.Optional;
import java.util.Set;

/**
 * Interfaz de servicio para la autenticación y gestión de usuarios
 */
public interface AuthService {
    
    /**
     * Autentica a un usuario con sus credenciales
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return Usuario autenticado o vacío si las credenciales son inválidas
     */
    Optional<User> authenticateUser(String username, String password);
    
    /**
     * Registra un nuevo usuario
     * @param username Nombre de usuario
     * @param email Correo electrónico
     * @param password Contraseña
     * @param roles Roles asignados al usuario
     * @return Usuario registrado
     */
    User registerUser(String username, String email, String password, Set<String> roles);
    
    /**
     * Verifica si un nombre de usuario ya existe
     * @param username Nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Verifica si un correo electrónico ya está registrado
     * @param email Correo electrónico a verificar
     * @return true si el correo existe, false en caso contrario
     */
    boolean existsByEmail(String email);
} 