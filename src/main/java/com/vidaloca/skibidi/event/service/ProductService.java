package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.ProductCategoryDto;
import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.ProductCategory;

public interface ProductService {
    Product addProduct(ProductDto productDto);
    Product updateProduct(ProductDto productDto, Integer id);
    void deleteProduct(Integer id);
    ProductCategory addCategory(ProductCategoryDto productCategoryDto);
}
