package com.cavosh.api_cafe.modules.usuarios.domain.ports.out;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;

import java.util.Optional;

public interface UsuarioRepositorioPuerto {
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario guardar(Usuario usuario);
}