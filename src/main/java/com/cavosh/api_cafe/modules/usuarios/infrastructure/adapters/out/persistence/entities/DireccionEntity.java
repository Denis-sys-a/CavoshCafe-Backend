package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tabla {@code usuario_direcciones}.
 *
 * Equivalencias con el dominio {@code Direccion}:
 * - etiqueta <-> etiqueta ("Casa", "Trabajo"...)
 * - es_predeterminada <-> esPrincipal
 * - calle, ciudad, distrito, referencia: columnas nuevas (ddl-auto=update las
 * crea, nulas
 * en filas antiguas).
 * - direccion (NOT NULL, original): texto completo "calle, distrito, ciudad"
 * que se
 * compone al guardar; es el que puede mostrarse tal cual (p. ej. en un pedido).
 * - latitud/longitud: solo persistencia, el dominio aún no las expone.
 *
 * El usuario se referencia por ID plano (FK fk_direccion_usuario en la BD) para
 * no
 * acoplar esta entidad a UsuarioEntity.
 */
@Entity
@Table(name = "usuario_direcciones")
@Getter
@Setter
@NoArgsConstructor
public class DireccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "etiqueta", nullable = false, length = 50)
    private String etiqueta;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "calle", length = 120)
    private String calle;

    @Column(name = "ciudad", length = 60)
    private String ciudad;

    @Column(name = "distrito", length = 60)
    private String distrito;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "latitud", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "es_predeterminada", nullable = false)
    private boolean predeterminada = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
