package com.cavosh.api_cafe.dto;

import com.cavosh.api_cafe.entity.ProductCategory;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String imageUrl;
    private ProductCategory category;
}
