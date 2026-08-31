package com.cavosh.api_cafe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("tokenAcceso")
    private String token;

    private String mensaje;
}