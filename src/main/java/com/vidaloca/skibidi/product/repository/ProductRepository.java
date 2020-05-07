package com.vidaloca.skibidi.product.repository;

import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product,Long> {
    Optional<Product> findByNameAndPriceAndProductCategory_NameAndEventUser(String name, BigDecimal price, String category, EventUser eventUser);
}
