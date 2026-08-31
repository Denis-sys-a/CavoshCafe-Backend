package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.ProductResponseDTO;
import java.util.List;

public interface FavoritoService {
    void agregarAFavoritos(Long usuarioId, Long productoId);

    void eliminarDeFavoritos(Long usuarioId, Long productoId);

    List<ProductResponseDTO> obtenerFavoritosPorUsuario(Long usuarioId);
}