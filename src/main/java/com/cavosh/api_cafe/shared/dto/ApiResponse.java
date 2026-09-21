package com.cavosh.api_cafe.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Clase genérica que estandariza el formato de las respuestas HTTP
 * de la API REST de CavoshCafe Backend.
 * <p>
 * Se utiliza tanto para respuestas exitosas ({@link #success(String, Object)})
 * como para respuestas de error ({@link #error(String, String, int)}),
 * garantizando una estructura consistente en todos los endpoints.
 *
 * @param <T>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String error;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private T data;

    // ------------------------------------------------------------------
    // Métodos estáticos de ayuda (factory methods)
    // ------------------------------------------------------------------

    /**
     * Construye una respuesta exitosa (HTTP 200) con un mensaje y un payload.
     *
     * @param message mensaje descriptivo de la operación
     * @param data    dato retornado por la operación
     * @param <T>     tipo del dato
     * @return instancia de {@link ApiResponse} representando el éxito
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return success(message, data, HttpStatus.OK.value());
    }

    /**
     * Construye una respuesta exitosa con un código de estado HTTP personalizado
     * (por ejemplo, 201 CREATED al registrar un recurso).
     *
     * @param message mensaje descriptivo de la operación
     * @param data    dato retornado por la operación
     * @param status  código de estado HTTP a utilizar
     * @param <T>     tipo del dato
     * @return instancia de {@link ApiResponse} representando el éxito
     */
    public static <T> ApiResponse<T> success(String message, T data, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .error(null)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    /**
     * Construye una respuesta exitosa con un código de estado HTTP personalizado,
     * usando {@link HttpStatus} en lugar del código numérico.
     *
     * @param message mensaje descriptivo de la operación
     * @param data    dato retornado por la operación
     * @param status  {@link HttpStatus} a utilizar
     * @param <T>     tipo del dato
     * @return instancia de {@link ApiResponse} representando el éxito
     */
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return success(message, data, status.value());
    }

    /**
     * Construye una respuesta de error con el código de estado HTTP indicado.
     *
     * @param error   código o tipo de error (por ejemplo: "NOT_FOUND", "BAD_REQUEST")
     * @param message mensaje descriptivo del error
     * @param status  código de estado HTTP asociado
     * @param <T>     tipo del dato (será {@code null} en este caso)
     * @return instancia de {@link ApiResponse} representando el error
     */
    public static <T> ApiResponse<T> error(String error, String message, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .message(message)
                .status(status)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
    }

    /**
     * Construye una respuesta de error usando {@link HttpStatus} en lugar del
     * código numérico. El nombre del error se toma de {@link HttpStatus#getReasonPhrase()}.
     *
     * @param message mensaje descriptivo del error
     * @param status  {@link HttpStatus} asociado
     * @param <T>     tipo del dato (será {@code null} en este caso)
     * @return instancia de {@link ApiResponse} representando el error
     */
    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return error(status.getReasonPhrase(), message, status.value());
    }
}