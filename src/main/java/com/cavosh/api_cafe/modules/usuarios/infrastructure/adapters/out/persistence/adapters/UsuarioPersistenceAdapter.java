package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.adapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.cavosh.api_cafe.modules.usuarios.domain.model.AuthProvider;
import com.cavosh.api_cafe.modules.usuarios.domain.model.Usuario;
import com.cavosh.api_cafe.modules.usuarios.domain.ports.out.UsuarioRepository;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.UsuarioEntity;
import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.repository.SpringDataUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioRepository {

    private final SpringDataUsuarioRepository repository;

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(this::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByCorreo(email).map(this::aDominio);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity guardado = repository.save(aEntidad(usuario));
        return aDominio(guardado);
    }

    // ------------------------------------------------------------------
    // Mapeo dominio <-> entidad
    // ------------------------------------------------------------------

    private UsuarioEntity aEntidad(Usuario modelo) {
        return UsuarioEntity.builder()
                .id(modelo.getId())
                .fullName(modelo.getFullName())
                .correo(modelo.getEmail())
                .password(modelo.getPassword())
                .authProvider(modelo.getAuthProvider() != null ? modelo.getAuthProvider() : AuthProvider.LOCAL)
                .telefono(modelo.getTelefono())
                .isVerified(Boolean.TRUE.equals(modelo.getIsVerified()))
                .build();
    }

    private Usuario aDominio(UsuarioEntity entidad) {
        return Usuario.builder()
                .id(entidad.getId())
                .fullName(entidad.getFullName())
                .email(entidad.getCorreo())
                .password(entidad.getPassword())
                .telefono(entidad.getTelefono())
                .authProvider(entidad.getAuthProvider())
                .isVerified(entidad.isVerified())
                .build();
    }
}