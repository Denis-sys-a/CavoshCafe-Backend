package com.cavosh.api_cafe.modules.auth.application.usecases;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;

public interface RegistrarUsuarioCasoUso {
    AuthResponseDTO ejecutar(RegisterRequestDTO request);
}