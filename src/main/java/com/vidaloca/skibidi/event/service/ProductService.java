package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.product.dto.ProductCategoryDto;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;

public interface ProductService {
    Product addProduct(ProductDto productDto);
    Product updateProduct(ProductDto productDto, Integer id);
    void deleteProduct(Integer id);
    ProductCategory addCategory(ProductCategoryDto productCategoryDto);
}
