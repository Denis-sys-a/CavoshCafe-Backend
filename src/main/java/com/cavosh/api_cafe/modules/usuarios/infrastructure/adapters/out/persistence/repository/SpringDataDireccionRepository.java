package com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.repository;

import com.cavosh.api_cafe.modules.usuarios.infrastructure.adapters.out.persistence.entities.DireccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataDireccionRepository extends JpaRepository<DireccionEntity, Long> {

    /** La predeterminada primero y luego por antigüedad. */
    List<DireccionEntity> findByUsuarioIdOrderByPredeterminadaDescIdAsc(Long usuarioId);

    Optional<DireccionEntity> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update DireccionEntity d set d.predeterminada = false "
            + "where d.usuarioId = :usuarioId and d.predeterminada = true")
    int desmarcarPredeterminadas(@Param("usuarioId") Long usuarioId);
}
