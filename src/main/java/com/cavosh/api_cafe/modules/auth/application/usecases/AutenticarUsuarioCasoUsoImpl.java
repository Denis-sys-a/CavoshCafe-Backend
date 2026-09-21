package com.cavosh.api_cafe.modules.auth.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cavosh.api_cafe.modules.auth.domain.exception.CuentaNoVerificadaException;
import com.cavosh.api_cafe.modules.auth.domain.exception.InvalidCredentialsException;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.LoginRequestDTO;
import com.cavosh.api_cafe.modules.usuarios.domain.model.AuthProvider;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;
import com.cavosh.api_cafe.shared.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutenticarUsuarioCasoUsoImpl implements AutenticarUsuarioCasoUso {

    private static final String ROL_POR_DEFECTO = "CLIENTE";
    private static final String MENSAJE_CREDENCIALES_INVALIDAS = "Correo o contraseña incorrectos";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponseDTO ejecutar(LoginRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();

        // Mensaje genérico: no revelamos si el correo existe o no en el sistema.
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(MENSAJE_CREDENCIALES_INVALIDAS));

        if (usuario.getAuthProvider() == AuthProvider.GOOGLE) {
            throw new InvalidCredentialsException(
                    "Esta cuenta se registró con Google. Inicia sesión con Google.");
        }

        if (usuario.getPassword() == null
                || !passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException(MENSAJE_CREDENCIALES_INVALIDAS);
        }

        if (!Boolean.TRUE.equals(usuario.getIsVerified())) {
            throw new CuentaNoVerificadaException(
                    "Debes verificar tu correo antes de iniciar sesión");
        }

        String rol = usuario.getRol() != null ? usuario.getRol() : ROL_POR_DEFECTO;
        String token = jwtTokenProvider.generarToken(usuario.getEmail(), rol);

        log.info("Inicio de sesión exitoso");

        return AuthResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .email(usuario.getEmail())
                .fullName(usuario.getFullName())
                .build();
    }
}
