package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
    private ProductService productService;
    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository,
                             ProductCategoryRepository productCategoryRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @GetMapping("/product")
    public List<Product> getAllProducts(){return (List<Product>) productRepository.findAll();}
    @PostMapping("/product")
    public GenericResponse addNewProductToEvent(@Valid @RequestBody ProductDto productDto ){
        Product p = productService.addProduct(productDto);
        return new GenericResponse("added successfully");
    }
    @GetMapping("/productCategories")
    public List<ProductCategory> showAllProducts(){
        return (List<ProductCategory>) productCategoryRepository.findAll();
    }
}
