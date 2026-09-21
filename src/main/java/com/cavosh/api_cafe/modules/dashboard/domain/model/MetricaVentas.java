package com.cavosh.api_cafe.modules.dashboard.domain.model;

import java.math.BigDecimal;

public class MetricaVentas {
    private BigDecimal totalVentas;
    private Long cantidadPedidos;
    private BigDecimal ticketPromedio;

    public MetricaVentas() {}

    public MetricaVentas(BigDecimal totalVentas, Long cantidadPedidos, BigDecimal ticketPromedio) {
        this.totalVentas = totalVentas;
        this.cantidadPedidos = cantidadPedidos;
        this.ticketPromedio = ticketPromedio;
    }

    // Getters y Setters
    public BigDecimal getTotalVentas() { return totalVentas; }
    public void setTotalVentas(BigDecimal totalVentas) { this.totalVentas = totalVentas; }

    public Long getCantidadPedidos() { return cantidadPedidos; }
    public void setCantidadPedidos(Long cantidadPedidos) { this.cantidadPedidos = cantidadPedidos; }

    public BigDecimal getTicketPromedio() { return ticketPromedio; }
    public void setTicketPromedio(BigDecimal ticketPromedio) { this.ticketPromedio = ticketPromedio; }
}