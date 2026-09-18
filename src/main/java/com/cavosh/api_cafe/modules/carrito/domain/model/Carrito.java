package com.cavosh.api_cafe.modules.carrito.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    private Long id;
    private Long usuarioId;

    @Builder.Default
    private List<CarritoItem> items = new ArrayList<>();

    private BigDecimal total;
}