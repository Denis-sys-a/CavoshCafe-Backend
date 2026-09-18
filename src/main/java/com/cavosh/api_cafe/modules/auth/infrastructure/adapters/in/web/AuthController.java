package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web;

import com.cavosh.api_cafe.modules.auth.application.usecases.AutenticarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.auth.application.usecases.RegistrarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.LoginRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrarUsuarioCasoUso registrarUsuarioCasoUso;
    private final AutenticarUsuarioCasoUso autenticarUsuarioCasoUso;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(registrarUsuarioCasoUso.ejecutar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(autenticarUsuarioCasoUso.ejecutar(request));
    }
}