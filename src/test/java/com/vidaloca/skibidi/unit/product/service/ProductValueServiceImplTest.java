package com.vidaloca.skibidi.unit.product.service;

import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import com.vidaloca.skibidi.product.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.product.service.ProductValueServiceImpl;
import com.vidaloca.skibidi.product.views.UserExpenses;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProductValueServiceImplTest {

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
    ProductValueServiceImpl service;

    Event event;
    User user;
    EventUser eventUser;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        user = new User();
        user.setId(1L);
        eventUser = new EventUser();
        eventUser.setId(1L);
        eventUser.setUser(user);
        eventUser.setEvent(event);

        event.getEventUsers().add(eventUser);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.of(eventUser));
    }

    @Test
    void totalAmountOfEvent() {
        //given
        BigDecimal price = new BigDecimal(10.00);
        Product product = new Product();
        product.setPrice(price);
        eventUser.getProducts().add(product);

        //when
        BigDecimal result = service.totalAmountOfEvent(1L, 1L);

        //then
        assertNotNull(result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void totalAmountOfEventThrowUserIsNotInEvent() {
        //given
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(UserIsNotInEventException.class, () -> {
            service.totalAmountOfEvent(1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action.", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void totalAmountOfProduct() {
        //given
        BigDecimal price = new BigDecimal(10.00);
        Product product = new Product();
        product.setPrice(price);
        eventUser.getProducts().add(product);

        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        //when
        BigDecimal result = service.totalAmountOfProduct(1L, 1L, 1L);

        //then
        assertNotNull(result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productRepository).should().findById(anyLong());
    }

    @Test
    void totalAmountOfProductThrowUserIsNotInEvent() {
        //given
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(UserIsNotInEventException.class, () -> {
            service.totalAmountOfProduct(1L, 1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action.", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productRepository).shouldHaveNoInteractions();
    }

    @Test
    void totalAmountOfCurrentUser() {
        //given
        BigDecimal price = new BigDecimal(10.00);
        Product product = new Product();
        product.setPrice(price);
        eventUser.getProducts().add(product);

        //when
        BigDecimal result = service.totalAmountOfCurrentUser(1L, 1L);

        //then
        assertNotNull(result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void totalAmountOfProductCategory() {
        //given
        BigDecimal price = new BigDecimal(10.00);
        Product product = new Product();
        product.setPrice(price);
        eventUser.getProducts().add(product);
        ProductCategory category = new ProductCategory();
        category.setId(1L);
        product.setProductCategory(category);

        given(productCategoryRepository.findById(1L)).willReturn(Optional.of(category));

        //when
        BigDecimal result = service.totalAmountOfProductCategory(1L, 1L, 1L);

        //then
        assertNotNull(result);
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productCategoryRepository).should().findById(anyLong());
    }

    @Test
    void totalAmountOfProductCategoryThrowUserIsNotInEvent() {
        //given
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(UserIsNotInEventException.class, () -> {
            service.totalAmountOfProductCategory(1L, 1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action.", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productCategoryRepository).shouldHaveNoInteractions();
    }

    @Test
    void totalAmountOfEventUser() {
        //given
        BigDecimal price = new BigDecimal(10.00);
        Product product = new Product();
        product.setPrice(price);
        eventUser.getProducts().add(product);

        //when
        BigDecimal result = service.totalAmountOfEventUser(1L, 1L, 1L);

        //then
        assertNotNull(result);
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should(times(2)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void totalAmountOfEventUserThrowUserIsNotInEvent() {
        //given
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(UserIsNotInEventException.class, () -> {
            service.totalAmountOfEventUser(1L, 1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action.", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
        then(productCategoryRepository).shouldHaveNoInteractions();
    }

    @Test
    void totalUsersExpenses() {
        //given
        BigDecimal price = new BigDecimal(10.00);
        Product product = new Product();
        product.setPrice(price);
        eventUser.getProducts().add(product);

        //when
        List<UserExpenses> result = service.totalUsersExpenses(1L, 1L);

        //then
        assertNotNull(result);
        then(userRepository).should(times(2)).findById(anyLong());
        then(eventRepository).should(times(2)).findById(anyLong());
        then(eventUserRepository).should(times(2)).findByUserAndEvent(any(User.class), any(Event.class));
    }

    @Test
    void totalUsersExpensesThrowUserIsNotInEvent() {
        //given
        given(eventUserRepository.findByUserAndEvent(user, event)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(UserIsNotInEventException.class, () -> {
            service.totalUsersExpenses(1L, 1L);
        });

        //then
        assertEquals("User with id: 1 is not in event with id: 1 and cannot make this action.", result.getMessage());
        then(userRepository).should().findById(anyLong());
        then(eventRepository).should().findById(anyLong());
        then(eventUserRepository).should().findByUserAndEvent(any(User.class), any(Event.class));
    }
}