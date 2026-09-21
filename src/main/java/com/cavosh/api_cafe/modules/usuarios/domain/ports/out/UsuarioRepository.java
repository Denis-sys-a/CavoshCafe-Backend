package com.cavosh.api_cafe.modules.usuarios.domain.ports.out;

import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;

import java.util.Optional;

/**
 * Puerto de salida de persistencia para usuarios.
 * (Renombrado desde UsuarioRepositorioPuerto para alinear con la
 * convención Puerto/Adaptador pedida en la Fase 1 de Autenticación.)
 */
public interface UsuarioRepository {
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario guardar(Usuario usuario);
}