package com.seibe.auth.application.service;

import com.seibe.auth.application.dto.JwtResponse;
import com.seibe.auth.application.dto.LoginRequest;
import com.seibe.auth.application.dto.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    String registerUser(SignupRequest signUpRequest);
} 