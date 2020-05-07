package com.vidaloca.skibidi.product.controller;

import com.vidaloca.skibidi.product.service.ProductValueService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
public class ProductValueController {

    private ProductValueService productValueService;

    @Autowired
    public ProductValueController(ProductValueService productValueService){
        this.productValueService = productValueService;
    }

    @GetMapping("/event/{eventId}/amount")
    public BigDecimal getTotalEventAmount(@PathVariable Long eventId, HttpServletRequest request){
        return productValueService.totalAmountOfEvent(eventId, CurrentUser.currentUserId(request));
    }
    @GetMapping("/event/{eventId}/user/{userId}/amount")
    public BigDecimal getTotalUserAmount(@PathVariable Long eventId, @PathVariable Long userId, HttpServletRequest request){
        return productValueService.totalAmountOfEventUser(eventId,userId, CurrentUser.currentUserId(request));
    }
    @GetMapping("/event/{eventId}/current_user/amount")
    public BigDecimal getTotalCurrentUserAmount(@PathVariable Long eventId,  HttpServletRequest request){
        return productValueService.totalAmountOfCurrentUser(eventId,CurrentUser.currentUserId(request));
    }
    @GetMapping("/event/{eventId}/product/{productId}/amount")
    public BigDecimal getTotalProductAmount(@PathVariable Long eventId, @PathVariable Long productId, HttpServletRequest request){
        return productValueService.totalAmountOfProduct(productId,eventId,CurrentUser.currentUserId(request));
    }
    @GetMapping("/event/{eventId}/product_category/{productCategoryId}/amount")
    public BigDecimal getTotalProductCategoryAmount(@PathVariable Long eventId, @PathVariable Long productCategoryId, HttpServletRequest request){
        return productValueService.totalAmountOfProduct(productCategoryId,eventId,CurrentUser.currentUserId(request));
    }
}
