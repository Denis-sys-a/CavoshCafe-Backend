package com.cavosh.api_cafe.modules.usuarios.domain.ports.out;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;

import java.util.List;

public interface DireccionRepositorioPuerto {
    Direccion guardar(Long usuarioId, Direccion direccion);
    List<Direccion> buscarPorUsuarioId(Long usuarioId);
}