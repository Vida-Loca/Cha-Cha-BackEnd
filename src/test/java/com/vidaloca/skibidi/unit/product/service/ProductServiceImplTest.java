package com.vidaloca.skibidi.unit.product.service;

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
import com.vidaloca.skibidi.product.service.ProductServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductCategoryRepository productCategoryRepository;
    @Mock(lenient = true)
    EventRepository eventRepository;
    @Mock(lenient = true)
    EventUserRepository eventUserRepository;
    @Mock(lenient = true)
    UserRepository userRepository;

    @InjectMocks
    ProductServiceImpl service;

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

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
    }

    @Test
    void addProductToEvent() {
        //given
        given(eventUserRepository.save(any(EventUser.class))).willReturn(eventUser);

        //when
        Product result = service.addProductToEvent(product, 1L, 1L);

        //then
        assertEquals(product, result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(eventUserRepository).should().save(any(EventUser.class));
    }

    @Test
    void addExistingProductToEvent() {
        //given
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(eventUserRepository.save(any(EventUser.class))).willReturn(eventUser);

        //when
        Product result = service.addExistingProductToEvent(1L, 1L, 1L);

        //then
        assertEquals(1, eventUser.getProducts().size());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productRepository).should().findById(anyLong());
        then(eventUserRepository).should().save(any(EventUser.class));
    }

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
        List<Product> returned = service.findAllEventProducts(event.getId());

        //then
        assertEquals(2, returned.size());
        verify(eventUserRepository, times(1)).findAllByEvent_Id(anyLong());
    }

    @Test
    void deleteProduct() {
        //given
        product.setEventUser(eventUser);
        eventUser.getProducts().add(product);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        String result = service.deleteProduct(event.getId(), product.getId(), user.getId());

        //then
        assertEquals("Successfully delete products", result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    void deleteProductFromAdmin() {
        //given
        eventUser.getProducts().add(product);
        eventUser.setAdmin(true);
        event.getEventUsers().add(eventUser);

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        String result = service.deleteProduct(event.getId(), product.getId(), user.getId());

        //then
        assertEquals("Successfully delete products", result);
        verify(userRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventUserRepository, times(1)).findByUserAndEvent(any(User.class), any(Event.class));
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).delete(any(Product.class));
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
        List<Product> returned = service.findUserEventProducts(event.getId(), user.getId());

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
        productDto.setQuantity(2);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(1L);
        productCategory.setName("Category");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setPrice(price);
        product.setProductCategory(productCategory);

        given(productRepository.findByNameAndPriceAndProductCategory_NameAndEventUser(productDto.getName(),productDto.getPrice(),productDto.getProductCategory(), eventUser))
                .willReturn(Optional.of(product));

        //when
        Product returned = service.addProduct(productDto,1L,1L);

        //then
        assertEquals("Test", returned.getName());
        assertEquals("20.20", returned.getPrice().toString());
        assertEquals("Category", returned.getProductCategory().getName());
        verify(productRepository, times(1))
                .findByNameAndPriceAndProductCategory_NameAndEventUser(anyString(), any(BigDecimal.class), anyString(), any(EventUser.class));
        verify(productCategoryRepository, times(1)).findByName(anyString());
        verify(productCategoryRepository, times(1)).save(any(ProductCategory.class));
    }

    @Test
    void updateProduct() {
        //given
        eventUser.getProducts().add(product);

        ProductDto productDto = new ProductDto();
        productDto.setPrice(new BigDecimal(10));
        productDto.setProductCategory("cat");
        productDto.setName("name");

        given(productRepository.save(any(Product.class))).willReturn(product);

        //when
        Product result = service.updateProduct(productDto, 1L, 1L, 1L);

        //then
        assertEquals("name", result.getName());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productRepository).should().save(any(Product.class));
    }

    @Test
    void updateProductReturnNullProduct() {
        //given
        Product pr = new Product();
        pr.setId(3L);
        eventUser.getProducts().add(pr);

        ProductDto productDto = new ProductDto();
        productDto.setPrice(new BigDecimal(10));
        productDto.setProductCategory("cat");
        productDto.setName("name");

        //when
        Exception result = assertThrows(ProductNotFoundException.class, () -> {
            service.updateProduct(productDto, 1L, 1L, 1L);
        });

        //then
        assertEquals("Product with id: 1 not found", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productRepository).shouldHaveNoMoreInteractions();
    }
}