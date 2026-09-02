package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.UsuarioDireccionRequestDTO;
import com.cavosh.api_cafe.dto.UsuarioDireccionResponseDTO;
import java.util.List;

public interface UsuarioDireccionService {
    List<UsuarioDireccionResponseDTO> obtenerDireccionesPorUsuario(String correo);

    UsuarioDireccionResponseDTO crearDireccion(String correo, UsuarioDireccionRequestDTO dto);

    void eliminarDireccion(String correo, Long direccionId);
}