package com.cavosh.api_cafe.modules.auth.application.usecases;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.LoginRequestDTO;

public interface AutenticarUsuarioCasoUso {
    AuthResponseDTO ejecutar(LoginRequestDTO request);
}