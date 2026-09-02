package com.cavosh.api_cafe.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucursalResponseDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private LocalTime horaApertura;
    private LocalTime horaCierre;
    private boolean activo;
}