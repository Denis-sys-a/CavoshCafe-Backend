package com.cavosh.api_cafe.shared.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cavosh.api_cafe.shared.security.exception.JwtAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTORIZACION = "Authorization";
    private static final String PREFIJO_BEARER = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = extraerToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            autenticarConToken(token, request);
            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException ex) {
            // Cubre JwtTokenExpiradoException y JwtTokenInvalidoException
            log.warn("Petición rechazada por token JWT inválido: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
            SecurityResponseWriter.escribirError(response, HttpStatus.UNAUTHORIZED, ex.getMessage(), objectMapper);

        } catch (UsernameNotFoundException ex) {
            // Token con firma válida, pero el usuario ya no existe.
            log.warn("Token JWT válido pero el usuario ya no existe: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
            SecurityResponseWriter.escribirError(response, HttpStatus.UNAUTHORIZED,
                    "El usuario asociado al token ya no existe", objectMapper);
        }
    }

    private void autenticarConToken(String token, HttpServletRequest request) {
        String email = jwtService.extraerUsername(token);

        boolean yaAutenticado = SecurityContextHolder.getContext().getAuthentication() != null;
        if (email == null || yaAutenticado) {
            return;
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if (!jwtService.esTokenValido(token, userDetails.getUsername())) {
            log.warn("El subject del token no coincide con el usuario cargado");
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extraerToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTORIZACION);
        if (header != null && header.startsWith(PREFIJO_BEARER)) {
            return header.substring(PREFIJO_BEARER.length());
        }
        return null;
    }
}