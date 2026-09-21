package com.cavosh.api_cafe.modules.auth.application.usecases;

import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.AuthResponseDTO;
import com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.dtos.LoginRequestDTO;
import org.springframework.stereotype.Service;

@Service // <--- Aquí es donde debe ir
public class AutenticarUsuarioCasoUsoImpl implements AutenticarUsuarioCasoUso {

    @Override
    public AuthResponseDTO ejecutar(LoginRequestDTO request) {
        // TODO: lógica real de login aquí (ver nota en la respuesta del asistente):
        // 1) buscar Usuario por email vía UsuarioRepositorioPuerto
        // 2) comparar request.getPassword() contra el hash con PasswordEncoder
        // 3) generar el token con JwtTokenProvider.generarToken(email, rol)
        // 4) lanzar InvalidCredentialsException si el usuario no existe o la
        //    contraseña no coincide
        return null;
    }
}