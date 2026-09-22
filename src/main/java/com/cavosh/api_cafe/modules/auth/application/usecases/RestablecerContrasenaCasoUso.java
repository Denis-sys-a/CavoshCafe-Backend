package com.cavosh.api_cafe.modules.auth.application.usecases;

public interface RestablecerContrasenaCasoUso {

    /**
     * Valida el resetToken emitido en el paso anterior, actualiza la
     * contraseña del usuario (hasheada con BCrypt) y marca el registro
     * de recuperación como usado para que no pueda reutilizarse.
     *
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.ResetTokenInvalidoException
     *         si el resetToken no existe, no corresponde al correo, ya fue
     *         usado o expiró
     */
    void ejecutar(String email, String resetToken, String newPassword);
}
