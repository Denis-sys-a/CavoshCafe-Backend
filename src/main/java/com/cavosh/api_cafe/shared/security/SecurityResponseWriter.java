package com.cavosh.api_cafe.shared.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.cavosh.api_cafe.shared.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public final class SecurityResponseWriter {

    private SecurityResponseWriter() {
    }

    public static void escribirError(HttpServletResponse response, HttpStatus status, String mensaje,
            ObjectMapper objectMapper) throws IOException {

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ApiResponse<Void> cuerpo = ApiResponse.error(mensaje, status);
        objectMapper.writeValue(response.getWriter(), cuerpo);
    }
}