package com.cavosh.api_cafe.modules.sucursales.domain.ports.out;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;

import java.util.List;
import java.util.Optional;

public interface SucursalRepositorioPuerto {
    List<Sucursal> obtenerTodas();
    Optional<Sucursal> obtenerPorId(Long id);
    Sucursal guardar(Sucursal sucursal);
    void eliminar(Long id);
}