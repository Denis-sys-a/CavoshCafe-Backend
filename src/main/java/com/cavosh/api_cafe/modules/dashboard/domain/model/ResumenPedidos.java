package com.cavosh.api_cafe.modules.dashboard.domain.model;

public class ResumenPedidos {
    private Long pedidosPendientes;
    private Long pedidosEnPreparacion;
    private Long pedidosCompletados;
    private Long pedidosCancelados;

    public ResumenPedidos() {}

    public ResumenPedidos(Long pedidosPendientes, Long pedidosEnPreparacion, 
                          Long pedidosCompletados, Long pedidosCancelados) {
        this.pedidosPendientes = pedidosPendientes;
        this.pedidosEnPreparacion = pedidosEnPreparacion;
        this.pedidosCompletados = pedidosCompletados;
        this.pedidosCancelados = pedidosCancelados;
    }

    // Getters y Setters
    public Long getPedidosPendientes() { return pedidosPendientes; }
    public void setPedidosPendientes(Long pedidosPendientes) { this.pedidosPendientes = pedidosPendientes; }

    public Long getPedidosEnPreparacion() { return pedidosEnPreparacion; }
    public void setPedidosEnPreparacion(Long pedidosEnPreparacion) { this.pedidosEnPreparacion = pedidosEnPreparacion; }

    public Long getPedidosCompletados() { return pedidosCompletados; }
    public void setPedidosCompletados(Long pedidosCompletados) { this.pedidosCompletados = pedidosCompletados; }

    public Long getPedidosCancelados() { return pedidosCancelados; }
    public void setPedidosCancelados(Long pedidosCancelados) { this.pedidosCancelados = pedidosCancelados; }
}