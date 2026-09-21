package com.cavosh.api_cafe.modules.auth.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cavosh.api_cafe.modules.auth.domain.exception.EmailAlreadyExistsException;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.GoogleAuthRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;
import com.cavosh.api_cafe.modules.usuarios.domain.model.AuthProvider;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;
import com.cavosh.api_cafe.shared.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarUsuarioCasoUsoImpl implements RegistrarUsuarioCasoUso {

    private static final String ROL_POR_DEFECTO = "CLIENTE";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EnviarCodigoVerificacionCasoUso enviarCodigoVerificacionCasoUso;

    // ------------------------------------------------------------------
    // Registro local (email + password)
    // ------------------------------------------------------------------

    @Override
    public AuthResponseDTO ejecutar(RegisterRequestDTO request) {
        String email = normalizarEmail(request.getEmail());

        usuarioRepository.buscarPorEmail(email).ifPresent(u -> {
            throw new EmailAlreadyExistsException("El correo ya se encuentra registrado");
        });

        Usuario nuevoUsuario = Usuario.builder()
                .fullName(request.getFullName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .telefono(request.getTelefono())
                .rol(ROL_POR_DEFECTO)
                .authProvider(AuthProvider.LOCAL)
                .isVerified(false)
                .build();

        Usuario guardado = usuarioRepository.guardar(nuevoUsuario);

        // El registro ya se persistió; si el envío del OTP falla (p. ej. un
        // corte temporal de SMTP) no queremos perder al usuario recién creado,
        // así que la falla se registra y el usuario puede reintentar el envío
        // vía POST /api/auth/enviar-codigo.
        enviarCodigoDeVerificacionSinBloquearRegistro(email);

        log.info("Usuario local registrado; pendiente de verificación de correo");

        // Sin token todavía: el usuario debe validar el OTP antes de poder
        // iniciar sesión (ver AutenticarUsuarioCasoUsoImpl).
        return AuthResponseDTO.builder()
                .email(guardado.getEmail())
                .fullName(guardado.getFullName())
                .build();
    }

    // ------------------------------------------------------------------
    // Registro / autenticación con Google
    // ------------------------------------------------------------------

    @Override
    public AuthResponseDTO ejecutarConGoogle(GoogleAuthRequestDTO request) {
        String email = normalizarEmail(request.getEmail());

        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseGet(() -> registrarNuevoUsuarioGoogle(email, request.getFullName()));

        String rol = usuario.getRol() != null ? usuario.getRol() : ROL_POR_DEFECTO;
        String token = jwtTokenProvider.generarToken(usuario.getEmail(), rol);

        log.info("Autenticación con Google exitosa");

        return AuthResponseDTO.builder()
                .token(token)
                .tipoToken("Bearer")
                .email(usuario.getEmail())
                .fullName(usuario.getFullName())
                .build();
    }

    private Usuario registrarNuevoUsuarioGoogle(String email, String fullName) {
        Usuario nuevoUsuario = Usuario.builder()
                .fullName(fullName)
                .email(email)
                .rol(ROL_POR_DEFECTO)
                .authProvider(AuthProvider.GOOGLE)
                .isVerified(true) // Google ya validó la propiedad del correo
                .build();

        return usuarioRepository.guardar(nuevoUsuario);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }

    private void enviarCodigoDeVerificacionSinBloquearRegistro(String email) {
        try {
            enviarCodigoVerificacionCasoUso.ejecutar(email);
        } catch (Exception e) {
            log.error("No se pudo enviar el código de verificación durante el registro", e);
        }
    }
}
