package com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgregarItemDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    /**
     * IDs de producto_opcion_valores elegidos (tamaño, tipo de leche, etc.).
     * Vacío o null = producto sin personalización.
     */
    @Builder.Default
    private List<Long> opcionesValoresIds = new ArrayList<>();
}