package com.cavosh.api_cafe.modules.auth.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio del código OTP de verificación de correo.
 * Independiente de JPA: el mapeo a {@code CodigoVerificacionEntity}
 * ocurre en el adaptador de persistencia.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodigoVerificacion {

    private Long id;
    private String email;
    private String codigo;
    private LocalDateTime expiracion;
    private boolean usado;
    private LocalDateTime fechaCreacion;

    public boolean estaExpirado() {
        return expiracion != null && expiracion.isBefore(LocalDateTime.now());
    }

    public void marcarComoUsado() {
        this.usado = true;
    }
}