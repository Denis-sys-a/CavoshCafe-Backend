package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.out.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Tabla {@code sucursales}.
 *
 * Diferencias con el modelo de dominio {@code Sucursal} (se resuelven en el
 * adaptador):
 * - activo (BD) <-> activa (dominio)
 * - hora_apertura + hora_cierre (BD) <-> horarioAtencion "HH:mm - HH:mm"
 * (dominio)
 * - telefono: columna nueva (el script original no la tenía; ddl-auto=update la
 * crea).
 * - latitud/longitud: solo persistencia, el dominio aún no las expone.
 */
@Entity
@Table(name = "sucursales")
@Getter
@Setter
@NoArgsConstructor
public class SucursalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "latitud", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "hora_apertura")
    private LocalTime horaApertura;

    @Column(name = "hora_cierre")
    private LocalTime horaCierre;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
