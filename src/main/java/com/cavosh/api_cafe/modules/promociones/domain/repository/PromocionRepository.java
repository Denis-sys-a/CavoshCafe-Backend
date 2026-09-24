package com.cavosh.api_cafe.modules.promociones.domain.repository;

import com.cavosh.api_cafe.modules.promociones.domain.model.Promocion;

import java.util.Optional;

/**
 * Puerto de salida (hexagonal) para la persistencia de {@link Promocion}.
 */
public interface PromocionRepository {

    Optional<Promocion> buscarPorCodigo(String codigo);

    Promocion guardar(Promocion promocion);
}
