package com.cavosh.api_cafe.dto;

import com.cavosh.api_cafe.entity.MetodoEntrega;
import com.cavosh.api_cafe.entity.MetodoPago;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La sucursalId es obligatoria")
    private Long sucursalId;

    private Long direccionId; // sera obligatorio cuando metodoEntrega = DELIVERY

    @NotNull(message = "El método de entrega es obligatorio")
    private MetodoEntrega metodoEntrega;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;

    private String codigoPromocional;
}
