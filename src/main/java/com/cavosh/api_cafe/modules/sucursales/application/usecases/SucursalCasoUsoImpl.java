package com.cavosh.api_cafe.modules.sucursales.application.usecases;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.in.ConsultarSucursalCasoUso;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.in.GestionarSucursalCasoUso;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.out.SucursalRepositorioPuerto;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos.SucursalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SucursalCasoUsoImpl implements ConsultarSucursalCasoUso, GestionarSucursalCasoUso {

    private final SucursalRepositorioPuerto sucursalRepositorioPuerto;

    @Override
    public List<Sucursal> obtenerTodas() {
        return sucursalRepositorioPuerto.obtenerTodas();
    }

    @Override
    public Sucursal obtenerPorId(Long id) {
        return sucursalRepositorioPuerto.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con ID: " + id));
    }

    @Override
    public Sucursal crear(SucursalDTO dto) {
        Sucursal sucursal = Sucursal.builder()
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .horarioAtencion(dto.getHorarioAtencion())
                .activa(dto.getActiva() != null ? dto.getActiva() : true)
                .build();
        return sucursalRepositorioPuerto.guardar(sucursal);
    }

    @Override
    public Sucursal actualizar(Long id, SucursalDTO dto) {
        Sucursal existente = obtenerPorId(id);
        existente.setNombre(dto.getNombre());
        existente.setDireccion(dto.getDireccion());
        existente.setTelefono(dto.getTelefono());
        existente.setHorarioAtencion(dto.getHorarioAtencion());
        if (dto.getActiva() != null) {
            existente.setActiva(dto.getActiva());
        }
        return sucursalRepositorioPuerto.guardar(existente);
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        sucursalRepositorioPuerto.eliminar(id);
    }
}