package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.exception.model.ProductNotFoundException;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import com.vidaloca.skibidi.product.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository,
                              EventRepository eventRepository, EventUserRepository eventUserRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Product addProductToEvent(Product product, Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        eu.getProducts().add(product);
        eventUserRepository.save(eu);
        return product;
    }

    @Override
    public Product addExistingProductToEvent(Long productId, Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        eu.getProducts().add(product);
        eventUserRepository.save(eu);
        return product;
    }

    @Override
    public List<Product> findAllEventProducts(Long eventId) {
        List<EventUser> euList = eventUserRepository.findAllByEvent_Id(eventId);
        List<Product> products = new ArrayList<>();
        euList.forEach(e -> products.addAll(e.getProducts()));
        return products;
    }

    @Override
    public String deleteProduct(Long eventId, Long productToDeleteId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eventUser = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        eventUser.getProducts().removeIf(p -> p.getId().equals(productToDeleteId));
        eventUserRepository.save(eventUser);
        return "Successfully delete products";

    }

    @Override
    public List<Product> findUserEventProducts(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eventUser = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return eventUser.getProducts();
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Optional<Product> temp = productRepository.findByNameAndPriceAndProductCategory_Name(productDto.getName(),productDto.getPrice(),productDto.getProductCategory());
        return temp.orElseGet(() -> Product.ProductBuilder.aProduct().withName(productDto.getName()).withPrice(getPrice(productDto.getPrice())).
                withProductCategory(getProductCategory(productDto.getProductCategory())).build());
    }

    private BigDecimal getPrice(BigDecimal price) {
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    private ProductCategory getProductCategory(String name) {
        Optional<ProductCategory> category = productCategoryRepository.findByName(name);
        return category.orElseGet(() -> addProductCategory(name));
    }

    private ProductCategory addProductCategory(String name) {
        ProductCategory productCategory = ProductCategory.ProductCategoryBuilder.aProductCategory().withName(name).build();
        return productCategoryRepository.save(productCategory);
    }
}
