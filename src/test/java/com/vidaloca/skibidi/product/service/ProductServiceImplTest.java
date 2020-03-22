package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductCategoryRepository productCategoryRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    EventUserRepository eventUserRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    ProductServiceImpl productService;

    User user;
    Event event;
    EventUser eventUser;
    Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        productService = new ProductServiceImpl(productRepository, productCategoryRepository,
                eventRepository, eventUserRepository, userRepository);


    }

    @Test
    void addProductToEvent() {
    }

    @Test
    void addExistingProductToEvent() {
    }

    @Test
    void findAllEventProducts() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void findUserEventProducts() {
    }

    @Test
    void addProduct() {
    }
}