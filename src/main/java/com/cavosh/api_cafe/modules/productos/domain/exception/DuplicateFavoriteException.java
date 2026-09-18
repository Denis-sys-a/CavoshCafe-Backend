package com.cavosh.api_cafe.modules.productos.domain.exception;

public class DuplicateFavoriteException extends RuntimeException {
    public DuplicateFavoriteException(String message) {
        super(message);
    }
}
