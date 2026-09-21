package com.cavosh.api_cafe.modules.auth.application.usecases; // o la carpeta de implementación/servicio

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service // <--- Aquí es donde debe ir
public class RegistrarUsuarioCasoUsoImpl implements RegistrarUsuarioCasoUso {

    @Override
    public AuthResponseDTO ejecutar(RegisterRequestDTO request) {
        // Tu lógica de registro aquí
        return null;
    }
}