package com.vidaloca.skibidi.integration.product.controller;

import com.vidaloca.skibidi.integration.BaseIT;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIT extends BaseIT {

    @Test
    @Transactional
    void getEventProducts() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/product", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("FoodEvent10")))
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    @Transactional
    void addProductToEvent_ExistingProduct() throws Exception {
        List<Product> before = (List<Product>) productRepository.findAll();
        BigDecimal price = new BigDecimal(1);
        ProductDto productDto = new ProductDto();
        productDto.setName("FoodEvent10");
        productDto.setQuantity(3);
        productDto.setProductCategory("FOOD");
        productDto.setPrice(price);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event/{eventId}/productNew", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isOk())
                .andDo(print());

        List<Product> after = (List<Product>) productRepository.findAll();
        assertEquals(before.size(), after.size());
    }

    @Test
    @Transactional
    void addProductToEvent_InvalidPrice() throws Exception {
        BigDecimal price = new BigDecimal(-1);
        ProductDto productDto = new ProductDto();
        productDto.setName("Invalid");
        productDto.setQuantity(3);
        productDto.setProductCategory("FOOD");
        productDto.setPrice(price);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event/{eventId}/productNew", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Transactional
    void addProductToEvent_NewProduct_ExistingCategory() throws Exception {
        List<ProductCategory> before = (List<ProductCategory>) productCategoryRepository.findAll();
        BigDecimal price = new BigDecimal(1.45);
        ProductDto productDto = new ProductDto();
        productDto.setName("NewTestProduct");
        productDto.setQuantity(3);
        productDto.setProductCategory("FOOD");
        productDto.setPrice(price);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event/{eventId}/productNew", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewTestProduct")))
                .andDo(print());

        List<ProductCategory> after = (List<ProductCategory>) productCategoryRepository.findAll();
        assertEquals(before.size(), after.size());
    }

    @Test
    @Transactional
    void addProductToEvent_NewProduct_NewCategory() throws Exception {
        List<ProductCategory> before = (List<ProductCategory>) productCategoryRepository.findAll();

        BigDecimal price = new BigDecimal(1.45);
        ProductDto productDto = new ProductDto();
        productDto.setName("NewTestProduct");
        productDto.setQuantity(3);
        productDto.setProductCategory("NEWCATEGORY");
        productDto.setPrice(price);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(post("/event/{eventId}/productNew", 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewTestProduct")))
                .andDo(print());

        List<ProductCategory> after = (List<ProductCategory>) productCategoryRepository.findAll();
        assertNotEquals(before.size(), after.size());
    }

    @Test
    @Transactional
    void updateProduct() throws Exception {
        List<Product> before = (List<Product>) productRepository.findAll();
        BigDecimal price = new BigDecimal(10);
        ProductDto productDto = new ProductDto();
        productDto.setName("UpdateTestProduct");
        productDto.setQuantity(3);
        productDto.setProductCategory("FOOD");
        productDto.setPrice(price);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/{eventId}/product/{productId}", 10, 10)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdateTestProduct")))
                .andDo(print());

        List<Product> after = (List<Product>) productRepository.findAll();
        assertEquals(before.size(), after.size());
    }

    @Test
    @Transactional
    void updateProductNotFound() throws Exception {
        BigDecimal price = new BigDecimal(10);
        ProductDto productDto = new ProductDto();
        productDto.setName("UpdateTestProduct");
        productDto.setQuantity(3);
        productDto.setProductCategory("FOOD");
        productDto.setPrice(price);

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(put("/event/{eventId}/product/{productId}", 10, 5)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(productDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Product with id: 5 not found")))
                .andDo(print());
    }

    @Test
    @Transactional
    void getEventUserProducts() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/user/{userId}/products", 10, 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void getMyEventUserProducts() throws Exception {
        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(get("/event/{eventId}/myproducts", 10)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @Transactional
    void deleteProductFromEvent() throws Exception {
        List<Product> before = (List<Product>) productRepository.findAll();

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(delete("/event/{eventId}/product", 10)
                .header("Authorization", "Bearer " + token)
                .param("productToDeleteId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Successfully delete products")))
                .andDo(print());

        List<Product> after = (List<Product>) productRepository.findAll();

        assertNotEquals(before, after);
    }

    @Test
    @Transactional
    void deleteProductFromEventNotAllowed() throws Exception {
        List<Product> before = (List<Product>) productRepository.findAll();

        String token = authenticateUser("testowy1", "password");

        mockMvc.perform(delete("/event/{eventId}/product", 11)
                .header("Authorization", "Bearer " + token)
                .param("productToDeleteId", "11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("User is not allowed to delete product")));

        List<Product> after = (List<Product>) productRepository.findAll();

        assertEquals(before, after);
    }
}