package com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.sucursales.domain.model.Sucursal;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.in.ConsultarSucursalCasoUso;
import com.cavosh.api_cafe.modules.sucursales.domain.ports.in.GestionarSucursalCasoUso;
import com.cavosh.api_cafe.modules.sucursales.infrastructure.adapters.in.web.dtos.SucursalDTO;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<List<Sucursal>>> obtenerTodas() {
        List<Sucursal> sucursales = consultarSucursalCasoUso.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success("Sucursales obtenidas exitosamente", sucursales));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sucursal>> obtenerPorId(@PathVariable Long id) {
        Sucursal sucursal = consultarSucursalCasoUso.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success("Sucursal obtenida exitosamente", sucursal));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Sucursal>> crear(@Valid @RequestBody SucursalDTO dto) {
        Sucursal sucursal = gestionarSucursalCasoUso.crear(dto);
        ApiResponse<Sucursal> response = ApiResponse.success(
                "Sucursal creada exitosamente", sucursal, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Sucursal>> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalDTO dto) {
        Sucursal sucursal = gestionarSucursalCasoUso.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Sucursal actualizada exitosamente", sucursal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        gestionarSucursalCasoUso.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Sucursal eliminada exitosamente", null));
    }
}