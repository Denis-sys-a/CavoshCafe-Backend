package com.cavosh.api_cafe.modules.auth.application.usecases;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.model.CodigoVerificacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoVerificacionRepository;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnviarCodigoVerificacionCasoUsoImpl implements EnviarCodigoVerificacionCasoUso {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final CodigoVerificacionRepository codigoRepository;
    private final EmailService emailService;

    @Value("${app.otp.expiration-minutes:10}")
    private int minutosExpiracion;

    @Value("${app.otp.cooldown-seconds:60}")
    private long segundosEspera;

    @Override
    @Transactional
    public void ejecutar(String email) {
        String emailNormalizado = email.trim().toLowerCase();

        verificarTiempoDeEspera(emailNormalizado);

        String codigo = generarCodigo();

        CodigoVerificacion nuevo = CodigoVerificacion.builder()
                .email(emailNormalizado)
                .codigo(codigo)
                .expiracion(LocalDateTime.now().plusMinutes(minutosExpiracion))
                .usado(false)
                .fechaCreacion(LocalDateTime.now())
                .build();

        codigoRepository.guardar(nuevo);

        // Si el envío falla, la excepción propaga y @Transactional revierte
        // el guardado, evitando códigos huérfanos que nadie recibió.
        emailService.enviarCodigoVerificacion(emailNormalizado, codigo);

        log.info("Código de verificación generado para el correo solicitado");
    }

    /**
     * Bloquea reenvíos consecutivos dentro de la ventana de espera configurada.
     */
    private void verificarTiempoDeEspera(String email) {
        codigoRepository.buscarUltimoVigentePorEmail(email).ifPresent(ultimo -> {
            LocalDateTime disponibleDesde = ultimo.getFechaCreacion().plusSeconds(segundosEspera);
            if (LocalDateTime.now().isBefore(disponibleDesde)) {
                throw new CodigoVerificacionInvalidoException(
                        "Debes esperar 1 minuto antes de solicitar un nuevo código");
            }
        });
    }

    /** OTP de 6 dígitos numéricos, con ceros a la izquierda preservados. */
    private String generarCodigo() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}