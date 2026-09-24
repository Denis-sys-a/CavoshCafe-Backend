package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.controllers;

import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.UsuarioResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Direccion;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.in.ConsultarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.in.RegistrarDireccionCasoUso;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.in.web.dtos.DireccionRequestDTO;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioDireccionController {

    private final ConsultarUsuarioCasoUso consultarUsuarioCasoUso;
    private final RegistrarDireccionCasoUso registrarDireccionCasoUso;

    // Un CLIENTE solo puede consultar SU PROPIO perfil (id debe coincidir con
    // el id embebido en su token); ADMIN y EMPLEADO pueden consultar el
    // perfil de cualquier usuario.
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = consultarUsuarioCasoUso.obtenerPorId(id);
        UsuarioResponseDTO response = UsuarioResponseDTO.fromDomain(usuario);
        return ResponseEntity.ok(ApiResponse.success("Usuario obtenido exitosamente", response));
    }

    @PostMapping("/{usuarioId}/direcciones")
    public ResponseEntity<ApiResponse<Direccion>> agregarDireccion(
            @PathVariable Long usuarioId,
            @Valid @RequestBody DireccionRequestDTO dto) {
        Direccion direccion = registrarDireccionCasoUso.agregarDireccion(usuarioId, dto);
        ApiResponse<Direccion> response = ApiResponse.success(
                "Dirección agregada exitosamente", direccion, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{usuarioId}/direcciones")
    public ResponseEntity<ApiResponse<List<Direccion>>> obtenerDirecciones(@PathVariable Long usuarioId) {
        List<Direccion> direcciones = registrarDireccionCasoUso.obtenerDireccionesPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Direcciones obtenidas exitosamente", direcciones));
    }
}