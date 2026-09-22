package com.cavosh.api_cafe.modules.auth.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cavosh.api_cafe.modules.auth.domain.exception.ResetTokenInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.model.CodigoRecuperacion;
import com.cavosh.api_cafe.modules.auth.domain.ports.out.CodigoRecuperacionRepository;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestablecerContrasenaCasoUsoImpl implements RestablecerContrasenaCasoUso {

    private final CodigoRecuperacionRepository codigoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void ejecutar(String email, String resetToken, String newPassword) {
        String emailNormalizado = email.trim().toLowerCase();

        CodigoRecuperacion encontrado = codigoRepository
                .buscarVigentePorEmailYToken(emailNormalizado, resetToken.trim())
                .orElseThrow(() -> new ResetTokenInvalidoException(
                        "El token de reseteo no es válido o ya fue utilizado"));

        if (encontrado.estaTokenExpirado()) {
            throw new ResetTokenInvalidoException("El token de reseteo ha expirado. Inicia el proceso nuevamente.");
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(emailNormalizado)
                .orElseThrow(() -> new ResetTokenInvalidoException(
                        "El token de reseteo no es válido o ya fue utilizado"));

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.guardar(usuario);

        encontrado.marcarComoUsado();
        codigoRepository.guardar(encontrado);

        log.info("Contraseña restablecida correctamente");
    }
}
