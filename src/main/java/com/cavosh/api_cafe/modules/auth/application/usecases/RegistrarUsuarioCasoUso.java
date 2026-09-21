package com.cavosh.api_cafe.modules.auth.application.usecases;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.GoogleAuthRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;

public interface RegistrarUsuarioCasoUso {

    /**
     * Registra un usuario local nuevo: valida que el correo no exista,
     * aplica hashing BCrypt a la contraseña, asigna AuthProvider.LOCAL e
     * isVerified = false, y dispara el envío del código OTP de verificación.
     *
     * @throws com.cavosh.api_cafe.modules.auth.domain.exception.EmailAlreadyExistsException
     *         si ya existe un usuario registrado con ese correo
     */
    AuthResponseDTO ejecutar(RegisterRequestDTO request);

    /**
     * Registra (si no existe) o autentica (si ya existe) a un usuario que
     * llega vía Google. isVerified se asigna automáticamente en true, dado
     * que Google ya validó la propiedad del correo.
     */
    AuthResponseDTO ejecutarConGoogle(GoogleAuthRequestDTO request);
}