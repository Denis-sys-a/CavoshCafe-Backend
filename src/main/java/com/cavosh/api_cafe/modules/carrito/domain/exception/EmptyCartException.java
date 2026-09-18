package com.cavosh.api_cafe.modules.carrito.domain.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }
}
