package com.cavosh.api_cafe.modules.pedidos.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private Long sucursalId;
    private Long direccionId;
    private String metodoPago;

    @NotNull(message = "Debe haber al menos un ítem en el pedido")
    private List<Long> productosIds;
}