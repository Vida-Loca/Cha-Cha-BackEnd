package com.vidaloca.skibidi.product;

import com.vidaloca.skibidi.product.dto.ProductCategoryDto;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Transactional
    @Test
    public void testAddProductFromExistingCategory() {
        ProductDto productDto = new ProductDto("Test", "20.01", "DRINK");
        Product product = productService.addProduct(productDto);
        List<ProductCategory> categories = (List<ProductCategory>) productCategoryRepository.findAll();
        Assert.assertEquals("Test", product.getName());
        Assert.assertEquals(2, categories.size());
    }

    @Transactional
    @Test
    public void testAddProductFromNotExistingCategory() {
        List<ProductCategory> productCategoryBefore = (List<ProductCategory>) productCategoryRepository.findAll();
        List<Product> productsBefore = (List<Product>) productRepository.findAll();
        ProductDto productDto = new ProductDto("Test", "20.0", "NewCategory");
        productService.addProduct(productDto);
        List<ProductCategory> productCategoryAfter = (List<ProductCategory>) productCategoryRepository.findAll();
        List<Product> productsAfter = (List<Product>) productRepository.findAll();
        Assert.assertNotEquals(productCategoryBefore.size(), productCategoryAfter.size());
        Assert.assertNotEquals(productsBefore.size(), productsAfter.size());
    }

    @Transactional
    @Test
    public void testAddNewProductCategory() {
        List<ProductCategory> productCategoryBefore = (List<ProductCategory>) productCategoryRepository.findAll();
        ProductCategoryDto productCategoryDto = new ProductCategoryDto("TestCategory");
        productService.addCategory(productCategoryDto);
        List<ProductCategory> productCategoryAfter = (List<ProductCategory>) productCategoryRepository.findAll();
        Assert.assertNotEquals(productCategoryBefore.size(), productCategoryAfter.size());
    }
}
