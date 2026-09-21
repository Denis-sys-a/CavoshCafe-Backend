package com.cavosh.api_cafe.modules.auth.application.usecases;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionExpiradoException;
import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.model.CodigoVerificacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoVerificacionRepository;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidarCodigoVerificacionCasoUsoImpl implements ValidarCodigoVerificacionCasoUso {

    private final CodigoVerificacionRepository codigoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public boolean ejecutar(String email, String codigo) {
        String emailNormalizado = email.trim().toLowerCase();

        CodigoVerificacion encontrado = codigoRepository
                .buscarVigentePorEmailYCodigo(emailNormalizado, codigo.trim())
                .orElseThrow(() -> new CodigoVerificacionInvalidoException(
                        "El código ingresado no es válido o ya fue utilizado"));

        if (encontrado.estaExpirado()) {
            throw new CodigoVerificacionExpiradoException("El código ha expirado. Solicita uno nuevo.");
        }

        encontrado.marcarComoUsado();
        codigoRepository.guardar(encontrado);

        marcarUsuarioComoVerificado(emailNormalizado);

        log.info("Código de verificación validado correctamente");
        return true;
    }

    /**
     * Al validar el OTP con éxito, el correo queda confirmado: se refleja en
     * el usuario (isVerified = true) para que pueda iniciar sesión.
     */
    private void marcarUsuarioComoVerificado(String email) {
        usuarioRepository.buscarPorEmail(email).ifPresent(usuario -> {
            if (!Boolean.TRUE.equals(usuario.getIsVerified())) {
                usuario.setIsVerified(true);
                usuarioRepository.guardar(usuario);
            }
        });
    }
}