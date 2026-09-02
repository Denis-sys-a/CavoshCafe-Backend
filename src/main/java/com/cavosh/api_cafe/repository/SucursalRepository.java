package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    List<Sucursal> findByActivoTrue();

    List<Sucursal> findByNombreContainingIgnoreCaseOrDireccionContainingIgnoreCase(String nombre, String direccion);
}