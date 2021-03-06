package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.model.Product;

import java.util.List;

public interface ProductService {

    Product addProductToEvent(ProductDto productDto, Long eventId, Long userId);

    String deleteProduct(Long eventId, Long productToDeleteId, Long userId);

    List<Product> findAllEventProducts(Long eventId);

    List<Product> findUserEventProducts(Long eventId, Long userId);

    Product updateProduct(ProductDto productDto, Long eventId, Long productId, Long userId);

}
