package com.cavosh.api_cafe.service;

import com.cavosh.api_cafe.dto.AuthResponseDTO;
import com.cavosh.api_cafe.dto.LoginRequestDTO;
import com.cavosh.api_cafe.dto.RegisterRequestDTO;
import com.cavosh.api_cafe.dto.VerificationRequestDTO;

public interface AuthService {
    AuthResponseDTO registrarUsuario(RegisterRequestDTO dto);

    AuthResponseDTO verificarCuenta(VerificationRequestDTO dto);

    AuthResponseDTO loginUsuario(LoginRequestDTO dto);
}
