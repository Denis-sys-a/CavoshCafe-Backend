package com.cavosh.api_cafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_direcciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDireccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "etiqueta", nullable = false, length = 50)
    private String etiqueta;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "latitud")
    private java.math.BigDecimal latitud;

    @Column(name = "longitud")
    private java.math.BigDecimal longitud;

    @Column(name = "es_predeterminada", nullable = false)
    @Builder.Default
    private boolean esPredeterminada = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
