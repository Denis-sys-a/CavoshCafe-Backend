package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.CodigoPromocional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CodigoPromocionalRepository extends JpaRepository<CodigoPromocional, Long> {
    Optional<CodigoPromocional> findByCodigoAndActivoTrue(String codigo);
}
