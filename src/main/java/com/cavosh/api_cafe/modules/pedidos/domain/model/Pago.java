package com.cavosh.api_cafe.modules.pedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    private Long id;
    private String metodoPago;
    private String estadoPago;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
}