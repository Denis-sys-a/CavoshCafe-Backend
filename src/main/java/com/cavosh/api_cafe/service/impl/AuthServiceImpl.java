package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.*;
import com.cavosh.api_cafe.entity.TokenVerificacion;
import com.cavosh.api_cafe.entity.Usuario;
import com.cavosh.api_cafe.exception.EmailAlreadyExistsException;
import com.cavosh.api_cafe.exception.InvalidCredentialsException;
import com.cavosh.api_cafe.exception.InvalidTokenException;
import com.cavosh.api_cafe.config.security.JwtTokenProvider;
import com.cavosh.api_cafe.repository.TokenVerificacionRepository;
import com.cavosh.api_cafe.repository.UsuarioRepository;
import com.cavosh.api_cafe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int TOKEN_VALIDITY_MINUTES = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final TokenVerificacionRepository tokenVerificacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponseDTO registrarUsuario(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new EmailAlreadyExistsException("Ya existe una cuenta registrada con este correo");
        }

        Usuario usuario = Usuario.builder()
                .nombreCompleto(dto.getNombreCompleto())
                .correo(dto.getCorreo())
                .contrasenaHash(passwordEncoder.encode(dto.getContrasena()))
                .telefono(dto.getTelefono())
                .activo(true)
                .verificado(false)
                .build();

        usuario = usuarioRepository.save(usuario);

        String token = generarTokenVerificacion();
        TokenVerificacion tokenVerificacion = TokenVerificacion.builder()
                .usuario(usuario)
                .token(token)
                .expiraEn(LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES))
                .build();
        tokenVerificacionRepository.save(tokenVerificacion);

        // TODO: enviar token por correo/SMS cuando se integre el servicio de
        // notificaciones
        return AuthResponseDTO.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .verificado(usuario.isVerificado())
                .mensaje("Registro exitoso. Revisa tu código de verificación.")
                .build();
    }

    @Override
    @Transactional
    public AuthResponseDTO verificarCuenta(VerificationRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new InvalidTokenException("Correo o token inválido"));

        TokenVerificacion tokenVerificacion = tokenVerificacionRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new InvalidTokenException("Correo o token inválido"));

        if (!tokenVerificacion.getUsuario().getId().equals(usuario.getId())) {
            throw new InvalidTokenException("Correo o token inválido");
        }

        if (tokenVerificacion.getExpiraEn().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("El token ha expirado, solicita uno nuevo");
        }

        usuario.setVerificado(true);
        usuarioRepository.save(usuario);

        return AuthResponseDTO.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .verificado(true)
                .mensaje("Cuenta verificada correctamente")
                .build();
    }

    @Override
    public AuthResponseDTO loginUsuario(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new InvalidCredentialsException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(dto.getContrasena(), usuario.getContrasenaHash())) {
            throw new InvalidCredentialsException("Correo o contraseña incorrectos");
        }

        if (!usuario.isActivo()) {
            throw new InvalidCredentialsException("Esta cuenta se encuentra inactiva");
        }

        String token = jwtTokenProvider.generarToken(usuario.getCorreo());

        return AuthResponseDTO.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .verificado(usuario.isVerificado())
                .token(token)
                .mensaje("Inicio de sesión exitoso")
                .build();
    }

    private String generarTokenVerificacion() {
        int codigo = 100000 + RANDOM.nextInt(900000); // 6 dígitos
        return String.valueOf(codigo);
    }
}