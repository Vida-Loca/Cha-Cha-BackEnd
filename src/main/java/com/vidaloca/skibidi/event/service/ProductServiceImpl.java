package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.ProductCategoryDto;
import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(Double.parseDouble(productDto.getPrice()));
        String category = productDto.getProductCategory();
        if (isCategoryInDb(category))
            product.setProductCategory(productCategoryRepository.findAllByName(category).get(0));
        else{
            ProductCategoryDto productCategoryDto = new ProductCategoryDto(category);
            ProductCategory pc = addCategory(productCategoryDto);
            product.setProductCategory(pc);
        }
        productRepository.save(product);
        return product;

    }

    @Override
    public Product updateProduct(ProductDto productDto, Integer id) {
        return null;
    }

    @Override
    public void deleteProduct(Integer id) {

    }

    @Override
    public ProductCategory addCategory(ProductCategoryDto productCategoryDto) {
        ProductCategory pc = new ProductCategory();
        pc.setName(productCategoryDto.getName().toUpperCase());
        productCategoryRepository.save(pc);
        return pc;
    }

    private boolean isCategoryInDb(String productCategory){
        return productCategoryRepository.findAllByName(productCategory.toUpperCase()).size() != 0;
    }
}
