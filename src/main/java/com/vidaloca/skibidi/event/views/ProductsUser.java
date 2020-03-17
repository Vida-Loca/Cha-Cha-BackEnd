package com.vidaloca.skibidi.event.views;

import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsUser {
    private ProductCategory productCategory;
    private List<Product> products;
    private User username;

}
