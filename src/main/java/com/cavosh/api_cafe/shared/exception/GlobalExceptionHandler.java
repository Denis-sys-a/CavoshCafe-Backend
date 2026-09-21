package com.cavosh.api_cafe.shared.exception;

import com.cavosh.api_cafe.modules.auth.domain.exception.CodigoVerificacionInvalidoException;
import com.cavosh.api_cafe.modules.auth.domain.exception.EmailAlreadyExistsException;
import com.cavosh.api_cafe.modules.auth.domain.exception.InvalidCredentialsException;
import com.cavosh.api_cafe.modules.auth.domain.exception.InvalidTokenException;
import com.cavosh.api_cafe.modules.carrito.domain.exception.EmptyCartException;
import com.cavosh.api_cafe.modules.carrito.domain.exception.InvalidPromoCodeException;
import com.cavosh.api_cafe.modules.productos.domain.exception.DuplicateFavoriteException;
import com.cavosh.api_cafe.modules.productos.domain.exception.ResourceNotFoundException;
import com.cavosh.api_cafe.shared.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones de la API.
 * <p>
 * Traduce las excepciones de negocio y de infraestructura lanzadas por los
 * casos de uso / controladores en respuestas HTTP estandarizadas usando
 * {@link ApiResponse}.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------------------------------------------------------------
    // 400 BAD REQUEST - Validación de argumentos (@Valid en los DTOs)
    // ------------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error de validación en la petición",
                HttpStatus.BAD_REQUEST.value()
        );
        response.setData(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ------------------------------------------------------------------
    // 400 BAD REQUEST - Reglas de negocio del carrito
    // ------------------------------------------------------------------
    @ExceptionHandler({EmptyCartException.class, InvalidPromoCodeException.class})
    public ResponseEntity<ApiResponse<Void>> handleCarritoBadRequest(RuntimeException ex) {
        log.warn("Solicitud inválida sobre el carrito: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ------------------------------------------------------------------
    // 400 BAD REQUEST - Código OTP de verificación inválido / expirado / reenvío prematuro
    // ------------------------------------------------------------------
    @ExceptionHandler(CodigoVerificacionInvalidoException.class)
    public ResponseEntity<ApiResponse<Void>> handleCodigoVerificacionInvalido(
            CodigoVerificacionInvalidoException ex) {
        log.warn("Código de verificación rechazado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ------------------------------------------------------------------
    // 401 UNAUTHORIZED - Credenciales o token inválidos
    // ------------------------------------------------------------------
    @ExceptionHandler({InvalidCredentialsException.class, InvalidTokenException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(RuntimeException ex) {
        log.warn("Fallo de autenticación: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // ------------------------------------------------------------------
    // 403 FORBIDDEN - Acceso denegado
    // ------------------------------------------------------------------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return buildErrorResponse("No tienes permisos para realizar esta acción", HttpStatus.FORBIDDEN);
    }

    // ------------------------------------------------------------------
    // 404 NOT FOUND - Recurso no encontrado
    // ------------------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // ------------------------------------------------------------------
    // 409 CONFLICT - Recursos duplicados
    // ------------------------------------------------------------------
    @ExceptionHandler({EmailAlreadyExistsException.class, DuplicateFavoriteException.class})
    public ResponseEntity<ApiResponse<Void>> handleConflictException(RuntimeException ex) {
        log.warn("Conflicto de datos: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // ------------------------------------------------------------------
    // 500 INTERNAL SERVER ERROR - Cualquier otra excepción no controlada
    // ------------------------------------------------------------------
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Error interno no controlado", ex);
        return buildErrorResponse("Ha ocurrido un error interno. Inténtalo nuevamente más tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Error interno no controlado", ex);
        return buildErrorResponse("Ha ocurrido un error interno. Inténtalo nuevamente más tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------
    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(String message, HttpStatus status) {
        ApiResponse<Void> response = ApiResponse.error(message, status);
        return ResponseEntity.status(status).body(response);
    }
}