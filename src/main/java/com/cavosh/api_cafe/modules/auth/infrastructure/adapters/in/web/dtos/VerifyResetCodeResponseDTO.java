package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResetCodeResponseDTO {

    private String resetToken;
    private int expiraEnMinutos;
}
