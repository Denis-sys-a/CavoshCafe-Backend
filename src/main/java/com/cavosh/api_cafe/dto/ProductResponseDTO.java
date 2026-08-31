package com.cavosh.api_cafe.dto;

import com.cavosh.api_cafe.entity.ProductCategory;
import com.cavosh.api_cafe.entity.ProductType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;

    @JsonProperty("producto")
    private String name;

    @JsonProperty("descripcion")
    private String description;

    @JsonProperty("precioBase")
    private BigDecimal basePrice;

    @JsonProperty("imagenUrl")
    private String imageUrl;

    @JsonProperty("categoria")
    private ProductCategory category;

    @JsonProperty("tipoProducto")
    private ProductType tipoProducto;
}
