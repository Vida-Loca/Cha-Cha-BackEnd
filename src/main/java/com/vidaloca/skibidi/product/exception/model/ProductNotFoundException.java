package com.vidaloca.skibidi.product.exception.model;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with id: " + id + " not found");
    }
}
