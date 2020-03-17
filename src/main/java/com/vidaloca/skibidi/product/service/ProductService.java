package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.UserCard;

import java.util.List;

public interface ProductService {

    UserCard addProductToEvent(Product product, Integer eventId, Long userId);

    String deleteProduct(Integer id, Integer productToDeleteId, Long userId);

    List<Product> findAllEventProducts(Integer id);

    List<Product> findUserEventProducts(Integer event_id, Long user_id);
}
