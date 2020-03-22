package com.vidaloca.skibidi.product.repository;

import com.vidaloca.skibidi.product.model.ProductCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory,Long> {
    Optional<ProductCategory> findByName(String findByName);
}
