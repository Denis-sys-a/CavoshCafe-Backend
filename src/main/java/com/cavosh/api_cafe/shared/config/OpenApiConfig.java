package com.cavosh.api_cafe.shared.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "CavoshCafe API", version = "v1", description = "API REST del backend de CavoshCafe (pedidos, productos, sucursales, autenticación, etc.)"), security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Pega aquí el Access Token devuelto por /api/auth/login o /api/auth/register (sin el prefijo 'Bearer ')")
public class OpenApiConfig {
}