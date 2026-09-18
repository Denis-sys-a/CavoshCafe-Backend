package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgregarItemDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    // IDs de producto_opcion_valores elegidos (tamaño, tipo de leche, etc.)
    private List<Long> opcionValorIds;
}
