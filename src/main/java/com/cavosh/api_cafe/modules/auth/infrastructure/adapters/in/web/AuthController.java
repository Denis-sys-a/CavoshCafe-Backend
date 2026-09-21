package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web;

import com.cavosh.api_cafe.modules.auth.application.usecases.AutenticarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.auth.application.usecases.RegistrarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.LoginRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrarUsuarioCasoUso registrarUsuarioCasoUso;
    private final AutenticarUsuarioCasoUso autenticarUsuarioCasoUso;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO resultado = registrarUsuarioCasoUso.ejecutar(request);
        ApiResponse<AuthResponseDTO> response = ApiResponse.success(
                "Usuario registrado exitosamente", resultado, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO resultado = autenticarUsuarioCasoUso.ejecutar(request);
        ApiResponse<AuthResponseDTO> response = ApiResponse.success("Inicio de sesión exitoso", resultado);
        return ResponseEntity.ok(response);
    }
}