package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.SucursalResponseDTO;
import java.util.List;

public interface SucursalService {
    List<SucursalResponseDTO> obtenerTodasActivas();

    List<SucursalResponseDTO> buscarPorTermino(String query);
}