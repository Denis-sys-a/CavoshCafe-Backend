package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.UsuarioEntity;

@Repository
public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}