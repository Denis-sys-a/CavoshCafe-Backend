package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web;

import com.cavosh.api_cafe.modules.auth.application.usecases.AutenticarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.auth.application.usecases.RegistrarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.auth.application.usecases.EnviarCodigoVerificacionCasoUso;
import com.cavosh.api_cafe.modules.auth.application.usecases.ValidarCodigoVerificacionCasoUso;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.EnviarCodigoRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.GoogleAuthRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.LoginRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.RegisterRequestDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.ValidarCodigoRequestDTO;
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
    private final EnviarCodigoVerificacionCasoUso enviarCodigoVerificacionCasoUso;
    private final ValidarCodigoVerificacionCasoUso validarCodigoVerificacionCasoUso;

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

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> loginConGoogle(
            @Valid @RequestBody GoogleAuthRequestDTO request) {
        AuthResponseDTO resultado = registrarUsuarioCasoUso.ejecutarConGoogle(request);
        ApiResponse<AuthResponseDTO> response = ApiResponse.success("Autenticación con Google exitosa", resultado);
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------------
    // Verificación de correo (OTP)
    // ------------------------------------------------------------------

    @PostMapping("/enviar-codigo")
    public ResponseEntity<ApiResponse<Void>> enviarCodigo(@Valid @RequestBody EnviarCodigoRequestDTO request) {
        enviarCodigoVerificacionCasoUso.ejecutar(request.getEmail());
        ApiResponse<Void> response = ApiResponse.success("Código enviado exitosamente al correo", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<ApiResponse<Boolean>> validarCodigo(@Valid @RequestBody ValidarCodigoRequestDTO request) {
        validarCodigoVerificacionCasoUso.ejecutar(request.getEmail(), request.getCodigo());
        ApiResponse<Boolean> response = ApiResponse.success("Código verificado exitosamente", true);
        return ResponseEntity.ok(response);
    }
}
