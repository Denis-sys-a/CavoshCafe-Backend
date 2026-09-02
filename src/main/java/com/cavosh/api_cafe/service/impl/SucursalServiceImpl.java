package com.cavosh.api_cafe.service.impl;

import com.cavosh.api_cafe.dto.SucursalResponseDTO;
import com.cavosh.api_cafe.entity.Sucursal;
import com.cavosh.api_cafe.repository.SucursalRepository;
import com.cavosh.api_cafe.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;

    @Override
    public List<SucursalResponseDTO> obtenerTodasActivas() {
        return sucursalRepository.findByActivoTrue().stream()
                .map(sucursal -> toResponseDTO(sucursal))
                .collect(Collectors.toList());
    }

    @Override
    public List<SucursalResponseDTO> buscarPorTermino(String query) {
        return sucursalRepository
                .findByNombreContainingIgnoreCaseOrDireccionContainingIgnoreCase(query, query)
                .stream()
                .map(sucursal -> toResponseDTO(sucursal))
                .collect(Collectors.toList());
    }

    private SucursalResponseDTO toResponseDTO(Sucursal sucursal) {
        return SucursalResponseDTO.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .direccion(sucursal.getDireccion())
                .latitud(sucursal.getLatitud())
                .longitud(sucursal.getLongitud())
                .horaApertura(sucursal.getHoraApertura())
                .horaCierre(sucursal.getHoraCierre())
                .activo(sucursal.isActivo())
                .build();
    }
}