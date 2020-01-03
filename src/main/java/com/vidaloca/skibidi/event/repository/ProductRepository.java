package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Integer> {
}
