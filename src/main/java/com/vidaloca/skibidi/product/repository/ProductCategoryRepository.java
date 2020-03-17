package com.vidaloca.skibidi.product.repository;

import com.vidaloca.skibidi.product.model.ProductCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory,Integer> {
    List<ProductCategory> findAllByName(String name);
}
