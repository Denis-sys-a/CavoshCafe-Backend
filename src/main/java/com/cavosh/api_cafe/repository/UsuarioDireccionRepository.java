package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.UsuarioDireccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioDireccionRepository extends JpaRepository<UsuarioDireccion, Long> {

    List<UsuarioDireccion> findByUsuarioId(Long usuarioId);

    Optional<UsuarioDireccion> findByUsuarioIdAndEsPredeterminadaTrue(Long usuarioId);
}