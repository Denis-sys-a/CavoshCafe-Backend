package com.cavosh.api_cafe.modules.usuarios.domain.ports.in;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;

public interface ConsultarUsuarioCasoUso {
    Usuario obtenerPorId(Long id);
    Usuario obtenerPorEmail(String email);
}