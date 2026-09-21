package com.cavosh.api_cafe.modules.promociones.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Promocion {
    private Long id;
    private String codigo;
    private String descripcion;
    private BigDecimal descuento;
    private TipoDescuento tipoDescuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean activo;

    public Promocion() {}

    public Promocion(Long id, String codigo, String descripcion, BigDecimal descuento, 
                     TipoDescuento tipoDescuento, LocalDateTime fechaInicio, 
                     LocalDateTime fechaFin, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.tipoDescuento = tipoDescuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
    }

    public boolean esVálida() {
        LocalDateTime ahora = LocalDateTime.now();
        return activo && ahora.isAfter(fechaInicio) && ahora.isBefore(fechaFin);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public TipoDescuento getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(TipoDescuento tipoDescuento) { this.tipoDescuento = tipoDescuento; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}