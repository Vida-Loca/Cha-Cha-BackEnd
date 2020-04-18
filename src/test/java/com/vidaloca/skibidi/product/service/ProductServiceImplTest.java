package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import com.vidaloca.skibidi.product.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        user = new User();
        user.setId(1L);
        event = new Event();
        event.setId(1L);
        eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setEvent(event);
        product = new Product();
        product.setId(1L);
        product.setName("Product");
    }

//    @Test
//    void addProductToEvent() {
//        //given
//        Optional<User> optionalUser = Optional.of(user);
//        Optional<Event> optionalEvent = Optional.of(event);
//        Optional<EventUser> optionalEventUser = Optional.of(eventUser);
//        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalEventUser);
//        when(eventUserRepository.save(any(EventUser.class))).thenReturn(eventUser);
//
//        //when
//        EventUser returned = productService.addProductToEvent(product, event.getId(), user.getId());
//
//        //then
//        assertEquals(1, returned.getProducts().size());
//        assertEquals("Product", returned.getProducts().get(0).getName());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(eventUserRepository, times(1)).save(any(EventUser.class));
//    }
//
//    @Test
//    void addExistingProductToEvent() {
//        //given
//        Optional<User> optionalUser = Optional.of(user);
//        Optional<Event> optionalEvent = Optional.of(event);
//        Optional<EventUser> optionalEventUser = Optional.of(eventUser);
//        Optional<Product> optionalProduct = Optional.of(product);
//        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
//        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
//        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalEventUser);
//        when(productRepository.findById(anyLong())).thenReturn(optionalProduct);
//        when(eventUserRepository.save(any(EventUser.class))).thenReturn(eventUser);
//
//        //when
//        EventUser returned = productService.addExistingProductToEvent(product.getId(), event.getId(), user.getId());
//
//        //then
//        assertEquals(1, returned.getProducts().size());
//        assertEquals("Product", returned.getProducts().get(0).getName());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(eventRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
//        verify(productRepository, times(1)).findById(anyLong());
//        verify(eventUserRepository, times(1)).save(any(EventUser.class));
//    }

    @Test
    void findAllEventProducts() {
        //given
        eventUser.getProducts().add(product);
        Product product1 = new Product();
        product1.setId(2L);
        product1.setName("Product1");
        EventUser eventUser1 = new EventUser();
        eventUser1.setId(2L);
        eventUser1.getProducts().add(product1);
        List<EventUser> eventUsers = new ArrayList<>();
        eventUsers.add(eventUser);
        eventUsers.add(eventUser1);

        when(eventUserRepository.findAllByEvent_Id(event.getId())).thenReturn(eventUsers);

        //when
        List<Product> returned = productService.findAllEventProducts(event.getId());

        //then
        assertEquals(2, returned.size());
        verify(eventUserRepository, times(1)).findAllByEvent_Id(anyLong());
    }

    @Test
    void deleteProduct() {
        //given
        eventUser.getProducts().add(product);
        int sizeBefore = eventUser.getProducts().size();
        Optional<User> optionalUser = Optional.of(user);
        Optional<Event> optionalEvent = Optional.of(event);
        Optional<EventUser> optionalEventUser = Optional.of(eventUser);

        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalEventUser);
        when(eventUserRepository.save(any(EventUser.class))).thenReturn(eventUser);

        //when
        String result = productService.deleteProduct(event.getId(), product.getId(), user.getId());
        int sizeAfter = eventUser.getProducts().size();

        //then
        assertEquals("Successfully delete products", result);
        assertEquals(0, eventUser.getProducts().size());
        assertNotEquals(sizeBefore, sizeAfter);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
        verify(eventUserRepository, times(1)).save(any(EventUser.class));
    }

    @Test
    void findUserEventProducts() {
        //given
        eventUser.getProducts().add(product);
        Optional<User> optionalUser = Optional.of(user);
        Optional<Event> optionalEvent = Optional.of(event);
        Optional<EventUser> optionalEventUser = Optional.of(eventUser);

        when(userRepository.findById(anyLong())).thenReturn(optionalUser);
        when(eventRepository.findById(anyLong())).thenReturn(optionalEvent);
        when(eventUserRepository.findByUserAndEvent(user, event)).thenReturn(optionalEventUser);

        //when
        List<Product> returned = productService.findUserEventProducts(event.getId(), user.getId());

        //then
        assertEquals(1, returned.size());
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void addExistingProduct() {
        //given
        BigDecimal price = new BigDecimal("20.20");
        ProductDto productDto = new ProductDto();
        productDto.setName("Test");
        productDto.setPrice(price);
        productDto.setProductCategory("Category");

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Category");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setPrice(price);
        product.setProductCategory(productCategory);
        Optional<Product> productOptional = Optional.of(product);

        when(productRepository.findByNameAndPriceAndProductCategory_Name(anyString(),
                any(BigDecimal.class), anyString())).thenReturn(productOptional);

        //when
        Product returned = productService.addProduct(productDto);

        //then
        assertEquals("Test", returned.getName());
        assertEquals("20.20", returned.getPrice().toString());
        assertEquals("Category", returned.getProductCategory().getName());
        verify(productRepository, times(1))
                .findByNameAndPriceAndProductCategory_Name(anyString(), any(BigDecimal.class), anyString());
        verify(productCategoryRepository, times(0)).findByName(anyString());
        verify(productCategoryRepository, times(0)).save(any(ProductCategory.class));
    }

    @Test
    void addProductFromExistingCategory() {
        //given
        BigDecimal price = new BigDecimal("20.20");
        ProductDto productDto = new ProductDto();
        productDto.setName("Test");
        productDto.setPrice(price);
        productDto.setProductCategory("Category");

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Category");

        Optional<Product> productOptional = Optional.empty();
        Optional<ProductCategory> productCategoryOptional = Optional.of(productCategory);

        when(productRepository.findByNameAndPriceAndProductCategory_Name(anyString(),
                any(BigDecimal.class), anyString())).thenReturn(productOptional);
        when(productCategoryRepository.findByName("Category")).thenReturn(productCategoryOptional);

        //when
        Product returned = productService.addProduct(productDto);

        //then
        assertEquals("Test", returned.getName());
        assertEquals("20.20", returned.getPrice().toString());
        assertEquals("Category", returned.getProductCategory().getName());
        verify(productRepository, times(1))
                .findByNameAndPriceAndProductCategory_Name(anyString(), any(BigDecimal.class), anyString());
        verify(productCategoryRepository, times(1)).findByName(anyString());
        verify(productCategoryRepository, times(0)).save(any(ProductCategory.class));
    }

    @Test
    void addProductFromNotExistingCategory() {
        //given
        BigDecimal price = new BigDecimal("20.20");
        ProductDto productDto = new ProductDto();
        productDto.setName("Test");
        productDto.setPrice(price);
        productDto.setProductCategory("Category");

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Category");

        Optional<Product> productOptional = Optional.empty();
        Optional<ProductCategory> productCategoryOptional = Optional.empty();

        when(productRepository.findByNameAndPriceAndProductCategory_Name(anyString(),
                any(BigDecimal.class), anyString())).thenReturn(productOptional);
        when(productCategoryRepository.findByName("Category")).thenReturn(productCategoryOptional);
        when(productCategoryRepository.save(any(ProductCategory.class))).thenReturn(productCategory);

        //when
        Product returned = productService.addProduct(productDto);

        //then
        assertEquals("Test", returned.getName());
        assertEquals("20.20", returned.getPrice().toString());
        assertEquals("Category", returned.getProductCategory().getName());
        verify(productRepository, times(1))
                .findByNameAndPriceAndProductCategory_Name(anyString(), any(BigDecimal.class), anyString());
        verify(productCategoryRepository, times(1)).findByName(anyString());
        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }
}