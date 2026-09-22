package com.cavosh.api_cafe.modules.auth.domain.ports.out;

/**
 * Puerto de salida para el envío de correos del módulo de autenticación.
 * La implementación concreta vive en infrastructure/adapters/out/mail.
 */
public interface EmailService {

    /**
     * Envía el código OTP de verificación al correo indicado.
     *
     * @param email  destinatario
     * @param codigo código de 6 dígitos
     */
    void enviarCodigoVerificacion(String email, String codigo);

    /**
     * Envía el código OTP de recuperación de contraseña al correo indicado.
     *
     * @param email  destinatario
     * @param codigo código de 6 dígitos
     */
    void enviarCodigoRecuperacion(String email, String codigo);
}
