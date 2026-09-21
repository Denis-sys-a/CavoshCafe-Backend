package com.cavosh.api_cafe.modules.usuarios.domain.model;

/**
 * Origen de autenticación de un usuario.
 * LOCAL: registrado con correo + contraseña propia.
 * GOOGLE: registrado/autenticado vía OAuth2 con Google (sin password local).
 */
public enum AuthProvider {
    LOCAL,
    GOOGLE
}