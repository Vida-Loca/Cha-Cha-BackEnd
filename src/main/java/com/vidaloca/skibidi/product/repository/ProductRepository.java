package com.vidaloca.skibidi.product.repository;

import com.vidaloca.skibidi.product.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Integer> {
}
