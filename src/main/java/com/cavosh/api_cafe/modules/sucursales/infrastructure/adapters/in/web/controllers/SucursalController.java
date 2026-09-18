package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.in.ConsultarSucursalCasoUso;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.in.GestionarSucursalCasoUso;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos.SucursalDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final ConsultarSucursalCasoUso consultarSucursalCasoUso;
    private final GestionarSucursalCasoUso gestionarSucursalCasoUso;

    @GetMapping
    public ResponseEntity<List<Sucursal>> obtenerTodas() {
        return ResponseEntity.ok(consultarSucursalCasoUso.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultarSucursalCasoUso.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Sucursal> crear(@Valid @RequestBody SucursalDTO dto) {
        return ResponseEntity.ok(gestionarSucursalCasoUso.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalDTO dto) {
        return ResponseEntity.ok(gestionarSucursalCasoUso.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionarSucursalCasoUso.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}