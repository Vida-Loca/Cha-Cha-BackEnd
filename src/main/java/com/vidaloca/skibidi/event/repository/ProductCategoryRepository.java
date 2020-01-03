package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.ProductCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory,Integer> {
    List<ProductCategory> findAllByName(String name);
}
