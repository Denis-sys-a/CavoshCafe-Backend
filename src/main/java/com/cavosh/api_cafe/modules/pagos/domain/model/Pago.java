package com.cavosh.api_cafe.modules.pagos.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pago {
    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private MetodoPago metodoPago;
    private EstadoPago estadoPago;
    private String transaccionId; // ID provisto por la pasarela externa
    private LocalDateTime fechaCreacion;

    public Pago() {}

    public Pago(Long id, Long pedidoId, BigDecimal monto, MetodoPago metodoPago, 
                EstadoPago estadoPago, String transaccionId, LocalDateTime fechaCreacion) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
        this.transaccionId = transaccionId;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }

    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }

    public String getTransaccionId() { return transaccionId; }
    public void setTransaccionId(String transaccionId) { this.transaccionId = transaccionId; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}