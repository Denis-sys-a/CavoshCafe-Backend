package com.cavosh.api_cafe.modules.pedidos.domain.model;

import com.cavosh.api_cafe.modules.pedidos.domain.enums.EstadoPedido;
import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private Long id;
    private Usuario usuario;
    private Sucursal sucursal;
    private Direccion direccion;
    private EstadoPedido estado;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;

    @Builder.Default
    private List<DetallePedido> detalles = new ArrayList<>();

    private Pago pago;
}