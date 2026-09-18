package com.cavosh.api_cafe.modules.carrito.domain.exception;

public class InvalidPromoCodeException extends RuntimeException {
    public InvalidPromoCodeException(String message) {
        super(message);
    }
}
