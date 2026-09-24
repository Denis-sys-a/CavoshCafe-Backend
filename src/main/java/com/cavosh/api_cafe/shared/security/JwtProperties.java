package com.cavosh.api_cafe.shared.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propiedades de configuración del módulo JWT.
 *
 * Se enlazan con el prefijo "app.jwt" para coincidir con
 * application.properties:
 * app.jwt.secret=${JWT_SECRET}
 * app.jwt.expiration-ms=86400000
 *
 * (Antes el prefijo era "jwt", lo cual no coincidía con las propiedades
 * reales usadas en el proyecto y hacía que siempre se usaran los valores
 * por defecto de esta clase en vez de los definidos en el archivo de
 * configuración o en la variable de entorno JWT_SECRET.)
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /**
     * Clave secreta usada para firmar/verificar los tokens (HMAC-SHA256).
     * Debe tener al menos 256 bits (32 caracteres) de longitud.
     * Se recomienda sobreescribirla siempre mediante la variable de entorno
     * JWT_SECRET en ambientes reales; el valor por defecto es solo un
     * fallback para desarrollo local.
     */
    private String secret = "claveSecretaBase64ParaJWTAutenticacionCafeteria2026";

    /**
     * Tiempo de expiración del Access Token, en milisegundos.
     */
    private long expirationMs = 86400000;
}