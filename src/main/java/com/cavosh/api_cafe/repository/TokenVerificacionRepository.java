package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.TokenVerificacion;
import com.cavosh.api_cafe.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenVerificacionRepository extends JpaRepository<TokenVerificacion, Long> {

    Optional<TokenVerificacion> findByToken(String token);

    Optional<TokenVerificacion> findTopByUsuarioOrderByCreatedAtDesc(Usuario usuario);
}
