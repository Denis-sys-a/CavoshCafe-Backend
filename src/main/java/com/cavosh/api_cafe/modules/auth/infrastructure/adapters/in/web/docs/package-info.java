/**
 * Interfaces de documentación OpenAPI/Swagger para los controladores del
 * módulo auth (AuthController y, en fases posteriores, el controlador OAuth2).
 * <p>
 * Convención a seguir en fases siguientes: por cada controlador REST se
 * define aquí una interfaz "...Docs" con los métodos anotados con
 * {@code @Operation}, {@code @ApiResponses}, etc. de springdoc-openapi;
 * el controlador implementa esa interfaz para mantener las anotaciones
 * de Swagger separadas de la lógica HTTP.
 * <p>
 * Requiere agregar la dependencia {@code springdoc-openapi-starter-webmvc-ui}
 * al pom.xml antes de escribir la primera interfaz (aún no incluida:
 * fuera del alcance de esta Fase 1).
 */
package com.cavosh.api_cafe.modules.auth.infrastructure.adapters.in.web.docs;