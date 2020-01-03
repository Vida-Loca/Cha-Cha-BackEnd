package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product,Integer> {
}
