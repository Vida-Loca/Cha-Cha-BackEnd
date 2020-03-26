package com.vidaloca.skibidi.product.controller;

import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.service.ProductService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/event/{eventId}/product")
    public List<Product> getEventProducts(@PathVariable Long eventId) {
        return productService.findAllEventProducts(eventId);
    }

    @CrossOrigin
    @PostMapping("/event/{eventId}/productNew")
    public Product addProductToEvent(@Valid @RequestBody ProductDto productDto, @PathVariable Long eventId,
                                       HttpServletRequest request) {
        return productService.addProductToEvent(productService.addProduct(productDto), eventId, CurrentUser.currentUserId(request));
    }

    @CrossOrigin
    @PostMapping("/event/{eventId}/product")
    public Product addProductToEvent(@RequestParam("productId") Long productId, @PathVariable Long eventId, HttpServletRequest request) {
        return productService.addExistingProductToEvent(productId, eventId, CurrentUser.currentUserId(request));
    }

    @GetMapping("/event/{eventId}/user/{userId}/products")
    public List<Product> getEventUserProducts(@PathVariable Long eventId, @PathVariable Long userId) {
        return productService.findUserEventProducts(eventId, userId);
    }

    @GetMapping("/event/{eventId}/myproducts")
    public List<Product> getMyEventUserProducts(@PathVariable Long eventId, HttpServletRequest request) {
        return productService.findUserEventProducts(eventId, CurrentUser.currentUserId(request));
    }

    @DeleteMapping("/event/{eventId}/product")
    public String deleteProductFromEvent(@PathVariable Long eventId, @RequestParam("productToDeleteId") Long productToDeleteId, HttpServletRequest request) {
        return productService.deleteProduct(eventId,productToDeleteId,CurrentUser.currentUserId(request));
    }
}
