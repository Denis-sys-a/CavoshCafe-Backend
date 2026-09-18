package com.cavosh.api_cafe.modules.carrito.application.usecases;

import com.cavosh.api_cafe.modules.carrito.domain.model.Carrito;
import com.cavosh.api_cafe.modules.carrito.domain.ports.in.CarritoCasoUso;
import com.cavosh.api_cafe.modules.carrito.domain.ports.out.CarritoRepositorioPuerto;
import com.cavosh.api_cafe.modules.carrito.infrastructure.adapters.in.web.dtos.AgregarItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarritoCasoUsoImpl implements CarritoCasoUso {

    private final CarritoRepositorioPuerto carritoRepositorioPuerto;

    @Override
    public Carrito obtenerCarritoPorUsuario(Long usuarioId) {
        return carritoRepositorioPuerto.obtenerPorUsuarioId(usuarioId);
    }

    @Override
    public Carrito agregarItem(Long usuarioId, AgregarItemDTO dto) {
        return carritoRepositorioPuerto.guardar(usuarioId, dto);
    }

    @Override
    public Carrito eliminarItem(Long usuarioId, Long itemId) {
        return carritoRepositorioPuerto.eliminarItem(usuarioId, itemId);
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        carritoRepositorioPuerto.vaciar(usuarioId);
    }
}