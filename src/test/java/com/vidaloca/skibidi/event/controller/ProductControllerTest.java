package com.vidaloca.skibidi.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.event.dto.ProductCategoryDto;
import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.ProductCategory;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .build();

       // productController = new ProductController(productService, productRepository);

    }

    @Test
    void getAllProducts() {
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("TEST");
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("JAPKO");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productController.getAllProducts();

        assertEquals("TEST", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getAllProductsStatus() throws Exception {
        mockMvc.perform(
                get("/product")
        ).andExpect(status().isOk());
    }

    @Test
    void addNewProductToEvent() {
        ProductCategoryDto productCategoryDto = new ProductCategoryDto();
        productCategoryDto.setName("NewCat");
        ProductDto productDto = new ProductDto();
        productDto.setName("NAME");
        productDto.setPrice("10.2");
        productDto.setProductCategory("Drink");
        Product product = new Product();
        product.setName("NAME");

        when(productService.addProduct(productDto)).thenReturn(product);

        GenericResponse str = productController.addNewProductToEvent(productDto);

        assertEquals("added successfully", str.getMessage());
        verify(productService, times(1)).addProduct(productDto);
    }

    @Test
    void addNewProductToEventStatus() throws Exception {
        ProductDto productDto = new ProductDto("product", "20.0", "Drink");

        mockMvc.perform(
                post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDto))
        ).andExpect(status().isOk());
    }

    @Test
    void showAllProducts() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setName("PRODUCTCAT");
        List<ProductCategory> list = new ArrayList<>();
        list.add(productCategory);

        when(productCategoryRepository.findAll()).thenReturn(list);

        List<ProductCategory> productCategories = productController.showAllProducts();

        assertEquals(1, productCategories.size());
    }

    @Test
    void showAllProductsStatus() throws Exception {
        mockMvc.perform(
                get("/productCategories")
        ).andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}