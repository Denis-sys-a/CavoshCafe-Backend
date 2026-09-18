package com.cavosh.api_cafe.modules.sucursales.domain.ports.in;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;

import java.util.List;

public interface ConsultarSucursalCasoUso {
    List<Sucursal> obtenerTodas();
    Sucursal obtenerPorId(Long id);
}