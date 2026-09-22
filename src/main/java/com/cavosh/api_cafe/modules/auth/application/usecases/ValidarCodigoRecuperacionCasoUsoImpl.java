package com.cavosh.api_cafe.modules.auth.application.usecases;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoRecuperacionExpiradoException;
import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoRecuperacionInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.model.CodigoRecuperacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoRecuperacionRepository;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.VerifyResetCodeResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidarCodigoRecuperacionCasoUsoImpl implements ValidarCodigoRecuperacionCasoUso {

    private final CodigoRecuperacionRepository codigoRepository;

    @Value("${app.password-reset.token-expiration-minutes:10}")
    private int minutosExpiracionToken;

    @Override
    @Transactional
    public VerifyResetCodeResponseDTO ejecutar(String email, String codigo) {
        String emailNormalizado = email.trim().toLowerCase();

        CodigoRecuperacion encontrado = codigoRepository
                .buscarVigentePorEmailYCodigo(emailNormalizado, codigo.trim())
                .orElseThrow(() -> new CodigoRecuperacionInvalidoException(
                        "El código ingresado no es válido o ya fue utilizado"));

        if (encontrado.estaCodigoExpirado()) {
            throw new CodigoRecuperacionExpiradoException("El código ha expirado. Solicita uno nuevo.");
        }

        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiracionToken = LocalDateTime.now().plusMinutes(minutosExpiracionToken);

        encontrado.marcarComoVerificado(resetToken, expiracionToken);
        codigoRepository.guardar(encontrado);

        log.info("Código de recuperación validado correctamente; resetToken emitido");

        return VerifyResetCodeResponseDTO.builder()
                .resetToken(resetToken)
                .expiraEnMinutos(minutosExpiracionToken)
                .build();
    }
}
