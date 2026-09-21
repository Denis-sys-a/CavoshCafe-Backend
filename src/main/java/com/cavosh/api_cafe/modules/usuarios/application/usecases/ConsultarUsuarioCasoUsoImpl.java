package com.cavosh.api_cafe.modules.usuarios.application.usecases;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.in.ConsultarUsuarioCasoUso;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultarUsuarioCasoUsoImpl implements ConsultarUsuarioCasoUso {

    private final UsuarioRepository usuarioRepositorioPuerto;

    @Override
    public Usuario obtenerPorId(Long id) {
        return usuarioRepositorioPuerto.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioRepositorioPuerto.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}