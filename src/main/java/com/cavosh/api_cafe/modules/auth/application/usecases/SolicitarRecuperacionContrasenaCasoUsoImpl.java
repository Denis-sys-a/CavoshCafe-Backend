package com.cavosh.api_cafe.modules.auth.application.usecases;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoRecuperacionInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.model.CodigoRecuperacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoRecuperacionRepository;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.EmailService;
import com.cavosh.api_cafe.modules.usuarios.domain.model.AuthProvider;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolicitarRecuperacionContrasenaCasoUsoImpl implements SolicitarRecuperacionContrasenaCasoUso {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final CodigoRecuperacionRepository codigoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Value("${app.password-reset.otp-expiration-minutes:15}")
    private int minutosExpiracion;

    @Value("${app.password-reset.cooldown-seconds:60}")
    private long segundosEspera;

    @Override
    @Transactional
    public void ejecutar(String email) {
        String emailNormalizado = email.trim().toLowerCase();

        Usuario usuario = usuarioRepository.buscarPorEmail(emailNormalizado).orElse(null);

        // Anti-enumeración: si el correo no existe, o el usuario se registró
        // con Google (no tiene contraseña local que resetear), no generamos
        // ni enviamos nada. El controller responde igual con mensaje genérico.
        if (usuario == null || usuario.getAuthProvider() == AuthProvider.GOOGLE) {
            log.info("Solicitud de recuperación para un correo no elegible; se ignora silenciosamente");
            return;
        }

        verificarTiempoDeEspera(emailNormalizado);

        String codigo = generarCodigo();

        CodigoRecuperacion nuevo = CodigoRecuperacion.builder()
                .email(emailNormalizado)
                .codigo(codigo)
                .expiracionCodigo(LocalDateTime.now().plusMinutes(minutosExpiracion))
                .verificado(false)
                .usado(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        codigoRepository.guardar(nuevo);

        // Si el envío falla, la excepción propaga y @Transactional revierte
        // el guardado, evitando códigos huérfanos que nadie recibió.
        emailService.enviarCodigoRecuperacion(emailNormalizado, codigo);

        log.info("Código de recuperación de contraseña generado para el correo solicitado");
    }

    /**
     * Bloquea reenvíos consecutivos dentro de la ventana de espera configurada.
     */
    private void verificarTiempoDeEspera(String email) {
        codigoRepository.buscarUltimoVigentePorEmail(email).ifPresent(ultimo -> {
            LocalDateTime disponibleDesde = ultimo.getFechaCreacion().plusSeconds(segundosEspera);
            if (LocalDateTime.now().isBefore(disponibleDesde)) {
                throw new CodigoRecuperacionInvalidoException(
                        "Debes esperar 1 minuto antes de solicitar un nuevo código");
            }
        });
    }

    /** OTP de 6 dígitos numéricos, con ceros a la izquierda preservados. */
    private String generarCodigo() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
