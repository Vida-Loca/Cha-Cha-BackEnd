package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.model.Product;
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

    @Autowired
    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/product")
    public List<Product> getAllProducts(){return (List<Product>) productRepository.findAll();}
    @PostMapping("/product")
    public String addNewProductToEvent(@Valid @RequestBody ProductDto productDto ){
        Product p = productService.addProduct(productDto);
        return "Successfully added product";
    }
}
