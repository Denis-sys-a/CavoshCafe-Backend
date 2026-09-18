package com.cavosh.api_cafe.modules.usuarios.domain.ports.in;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.DireccionRequestDTO;

import java.util.List;

public interface RegistrarDireccionCasoUso {
    Direccion agregarDireccion(Long usuarioId, DireccionRequestDTO dto);
    List<Direccion> obtenerDireccionesPorUsuario(Long usuarioId);
}