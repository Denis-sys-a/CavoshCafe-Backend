package com.cavosh.api_cafe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private boolean verificado;
    private String mensaje;
}
