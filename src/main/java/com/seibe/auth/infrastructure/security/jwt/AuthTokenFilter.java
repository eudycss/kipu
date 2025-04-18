package com.seibe.auth.infrastructure.security.jwt;

import com.seibe.auth.infrastructure.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            logger.debug("JWT en solicitud: {}", jwt != null ? "Presente" : "Ausente");
            
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                logger.debug("Nombre de usuario del token: {}", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Registrar las autoridades del usuario
                String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
                logger.debug("Autoridades del usuario {}: {}", username, authorities);
                
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Autenticación establecida para el usuario: {}", username);
            } else if (jwt != null) {
                logger.warn("JWT inválido en la solicitud");
            }
            
            // Registrar la URI solicitada
            logger.debug("URI solicitada: {}", request.getRequestURI());
            
        } catch (Exception e) {
            logger.error("No se puede establecer la autenticación del usuario: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        logger.debug("Encabezado de autorización: {}", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
} 