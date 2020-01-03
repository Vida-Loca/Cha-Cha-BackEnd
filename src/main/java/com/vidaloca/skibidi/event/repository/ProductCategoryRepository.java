package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.ProductCategory;
import org.springframework.data.repository.CrudRepository;

public interface ProductCategoryRepository extends CrudRepository<ProductCategory,Integer> {
}
