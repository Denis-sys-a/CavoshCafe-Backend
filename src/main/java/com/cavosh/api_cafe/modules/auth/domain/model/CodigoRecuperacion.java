package com.cavosh.api_cafe.modules.auth.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio del código OTP de recuperación de contraseña.
 * Independiente de JPA: el mapeo a {@code CodigoRecuperacionEntity}
 * ocurre en el adaptador de persistencia.
 * <p>
 * Es un modelo separado de {@link CodigoVerificacion} (verificación de
 * correo) a propósito: aunque ambos son OTPs de 6 dígitos, mezclarlos en
 * la misma tabla permitiría, en teoría, usar un código emitido para un
 * propósito (verificar correo) en el otro (resetear contraseña).
 * <p>
 * Sigue el flujo de 2 pasos: primero se valida el {@code codigo} (OTP)
 * y, una vez verificado, se emite un {@code token} de reseteo opaco de
 * vida corta con el que se realiza el cambio de contraseña final.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodigoRecuperacion {

    private Long id;
    private String email;
    private String codigo;
    private String token;
    private LocalDateTime expiracionCodigo;
    private LocalDateTime expiracionToken;
    private boolean verificado;
    private boolean usado;
    private LocalDateTime fechaCreacion;

    public boolean estaCodigoExpirado() {
        return expiracionCodigo != null && expiracionCodigo.isBefore(LocalDateTime.now());
    }

    public boolean estaTokenExpirado() {
        return expiracionToken == null || expiracionToken.isBefore(LocalDateTime.now());
    }

    /**
     * Marca el OTP como verificado y adjunta el token de reseteo emitido,
     * junto con su propia ventana de expiración (más corta que la del OTP).
     */
    public void marcarComoVerificado(String token, LocalDateTime expiracionToken) {
        this.verificado = true;
        this.token = token;
        this.expiracionToken = expiracionToken;
    }

    public void marcarComoUsado() {
        this.usado = true;
    }
}
