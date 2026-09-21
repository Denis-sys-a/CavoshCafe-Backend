package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de autenticación con Google.
 * <p>
 * NOTA (alcance de la Fase 2): este DTO todavía no valida un idToken de
 * Google contra los servidores de Google — eso requiere un puerto/adaptador
 * adicional (p. ej. un {@code GoogleTokenVerifier}) que queda fuera del
 * alcance de esta fase, centrada en casos de uso y servicios de dominio. Por
 * ahora se asume que el cliente (app móvil) ya validó la sesión de Google vía
 * su SDK y solo reenvía el correo y nombre ya confirmados por Google.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthRequestDTO {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    private String email;

    @NotBlank(message = "El nombre es obligatorio")
    private String fullName;
}
