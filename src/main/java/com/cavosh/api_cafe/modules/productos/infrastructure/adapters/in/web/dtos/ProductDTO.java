package com.cavosh.api_cafe.modules.productos.infrastructure.adapters.in.web.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;

import com.cavosh.api_cafe.modules.productos.infrastructure.adapters.out.persistence.entities.ProductCategoryEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "El precio base es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal basePrice;

    private String imageUrl;

    @NotNull(message = "La categoría es obligatoria")
    private ProductCategoryEntity category;
}
