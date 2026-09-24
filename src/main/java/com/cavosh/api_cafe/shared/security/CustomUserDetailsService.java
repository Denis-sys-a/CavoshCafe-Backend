package com.cavosh.api_cafe.shared.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de {@link UserDetailsService} requerida por Spring
 * Security. Carga al usuario a través del puerto de dominio
 * {@link UsuarioRepository} (no accede directamente a JPA), manteniendo
 * la capa de seguridad desacoplada de la infraestructura de persistencia.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("El correo no puede estar vacío");
        }

        String emailNormalizado = email.trim().toLowerCase();

        Usuario usuario = usuarioRepository.buscarPorEmail(emailNormalizado)
                .orElseThrow(() -> {
                    log.warn("Intento de autenticación con un correo no registrado");
                    return new UsernameNotFoundException(
                            "No existe un usuario registrado con el correo: " + emailNormalizado);
                });

        return new CustomUserDetails(usuario);
    }
}
