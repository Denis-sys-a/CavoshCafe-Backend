package com.cavosh.api_cafe.modules.auth.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.model.CodigoVerificacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoVerificacionRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidarCodigoVerificacionCasoUsoImpl implements ValidarCodigoVerificacionCasoUso {

    private final CodigoVerificacionRepositoryPort codigoRepository;

    @Override
    @Transactional
    public boolean ejecutar(String email, String codigo) {
        String emailNormalizado = email.trim().toLowerCase();

        CodigoVerificacion encontrado = codigoRepository
                .buscarVigentePorEmailYCodigo(emailNormalizado, codigo.trim())
                .orElseThrow(() -> new CodigoVerificacionInvalidoException(
                        "El código ingresado no es válido o ya fue utilizado"));

        if (encontrado.estaExpirado()) {
            throw new CodigoVerificacionInvalidoException("El código ha expirado. Solicita uno nuevo.");
        }

        encontrado.marcarComoUsado();
        codigoRepository.guardar(encontrado);

        log.info("Código de verificación validado correctamente");
        return true;
    }
}