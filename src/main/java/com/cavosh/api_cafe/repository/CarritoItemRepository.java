package com.cavosh.api_cafe.repository;

import com.cavosh.api_cafe.entity.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
}
