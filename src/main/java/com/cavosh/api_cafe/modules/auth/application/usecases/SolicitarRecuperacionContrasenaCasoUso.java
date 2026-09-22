package com.cavosh.api_cafe.modules.auth.application.usecases;

public interface SolicitarRecuperacionContrasenaCasoUso {

    /**
     * Genera un OTP de recuperación de contraseña, lo persiste y lo envía
     * al correo indicado si corresponde.
     * <p>
     * Por seguridad (anti-enumeración de usuarios), si el correo no existe
     * o pertenece a un usuario registrado con {@code AuthProvider.GOOGLE}
     * (sin contraseña local que resetear), el método retorna normalmente
     * sin generar ni enviar nada — el llamador (controller) siempre debe
     * responder con el mismo mensaje genérico.
     *
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.CodigoRecuperacionInvalidoException
     *         si aún no se cumple el tiempo mínimo de espera para un reenvío
     */
    void ejecutar(String email);
}
