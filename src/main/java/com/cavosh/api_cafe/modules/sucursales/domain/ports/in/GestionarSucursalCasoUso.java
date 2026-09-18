package com.cavosh.api_cafe.modules.sucursales.domain.ports.in;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos.SucursalDTO;

public interface GestionarSucursalCasoUso {
    Sucursal crear(SucursalDTO dto);
    Sucursal actualizar(Long id, SucursalDTO dto);
    void eliminar(Long id);
}