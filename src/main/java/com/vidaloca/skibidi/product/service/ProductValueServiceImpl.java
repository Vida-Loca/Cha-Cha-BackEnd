package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.product.exception.model.ProductNotFoundException;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.ProductCategory;
import com.vidaloca.skibidi.product.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.product.views.UserExpenses;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductValueServiceImpl implements ProductValueService {

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private UserRepository userRepository;

    @Autowired
    public ProductValueServiceImpl(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository,
                              EventRepository eventRepository, EventUserRepository eventUserRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BigDecimal totalAmountOfEvent(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if(eventUserRepository.findByUserAndEvent(user, event).isEmpty())
            throw new UserIsNotInEventException(user.getId(), event.getId());
        BigDecimal total = new BigDecimal("0.00");
        for (EventUser eu : event.getEventUsers()){
            for (Product p: eu.getProducts()){
                total = total.add(p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
            }
        }
        return total;
    }

    @Override
    public BigDecimal totalAmountOfProduct(Long productId, Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if(eventUserRepository.findByUserAndEvent(user, event).isEmpty())
            throw new UserIsNotInEventException(user.getId(), event.getId());
        Product product = productRepository.findById(productId).orElseThrow(()-> new ProductNotFoundException(productId));
        return product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
    }

    @Override
    public BigDecimal totalAmountOfCurrentUser(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        BigDecimal total = new BigDecimal("0.00");
        for (Product p : eu.getProducts()){
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
        }
        return total;
    }

    @Override
    public BigDecimal totalAmountOfProductCategory(Long productCategoryId, Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if(eventUserRepository.findByUserAndEvent(user, event).isEmpty())
            throw new UserIsNotInEventException(user.getId(), event.getId());
        ProductCategory pc = productCategoryRepository.findById(productCategoryId).orElseThrow(()-> new ProductNotFoundException(productCategoryId));
        BigDecimal total = new BigDecimal("0.00");
        for (EventUser eu : event.getEventUsers()){
            for (Product p: eu.getProducts()){
                if (p.getProductCategory().equals(pc))
                    total = total.add(p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
            }
        }
        return total;
    }

    @Override
    public BigDecimal totalAmountOfEventUser(Long eventId, Long userId, Long currentUserId) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if(eventUserRepository.findByUserAndEvent(currentUser, event).isEmpty())
            throw new UserIsNotInEventException(currentUser.getId(), event.getId());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        BigDecimal total = new BigDecimal("0.00");
        for (Product p : eu.getProducts()){
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
        }
        return total;
    }

    @Override
    public List<UserExpenses> totalUsersExpenses(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if(eventUserRepository.findByUserAndEvent(user, event).isEmpty())
            throw new UserIsNotInEventException(user.getId(), event.getId());
        List<UserExpenses> userExpensesList = new ArrayList<>();
        UserExpenses userExpenses = new UserExpenses();
        for (EventUser eu : event.getEventUsers()){
            userExpenses.setEventUser(eu);
            userExpenses.setExpenses(totalAmountOfCurrentUser(eventId,userId));
            userExpensesList.add(userExpenses);
        }
        return userExpensesList;
    }
}
