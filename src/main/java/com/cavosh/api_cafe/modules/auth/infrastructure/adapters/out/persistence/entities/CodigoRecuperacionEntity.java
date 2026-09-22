package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.out.persistence.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "codigos_recuperacion_password",
        indexes = {
                @Index(name = "idx_recuperacion_correo_usado", columnList = "correo, usado, fecha_creacion"),
                @Index(name = "idx_recuperacion_token", columnList = "token")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodigoRecuperacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "correo", nullable = false, length = 150)
    private String correo;

    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;

    @Column(name = "token", length = 36)
    private String token;

    @Column(name = "fecha_expiracion_codigo", nullable = false)
    private LocalDateTime fechaExpiracionCodigo;

    @Column(name = "fecha_expiracion_token")
    private LocalDateTime fechaExpiracionToken;

    @Builder.Default
    @Column(name = "verificado", nullable = false)
    private boolean verificado = false;

    @Builder.Default
    @Column(name = "usado", nullable = false)
    private boolean usado = false;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }
}
