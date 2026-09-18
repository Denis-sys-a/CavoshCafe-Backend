package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos.SucursalResponseDTO;
import com.cavosh.api_cafe.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalResponseDTO>> obtenerTodas() {
        return ResponseEntity.ok(sucursalService.obtenerTodasActivas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<SucursalResponseDTO>> buscar(@RequestParam String query) {
        return ResponseEntity.ok(sucursalService.buscarPorTermino(query));
    }
}