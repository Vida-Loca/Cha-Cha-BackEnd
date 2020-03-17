package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.product.model.UserCard;
import com.vidaloca.skibidi.product.repository.ProductCategoryRepository;
import com.vidaloca.skibidi.product.repository.ProductRepository;
import com.vidaloca.skibidi.product.repository.UserCardRepository;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;
    private ProductCategoryRepository productCategoryRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private UserCardRepository userCardRepository;
    private UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository,
                              EventRepository eventRepository, EventUserRepository eventUserRepository,
                              UserCardRepository userCardRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
        this.userCardRepository = userCardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserCard addProductToEvent(Product product, Integer eventId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
     //   if (user == null)
    //        return "Unexpected failure";
        Event event = eventRepository.findById(eventId).orElse(null);
    //    if (event == null)
  //          return "Event doesn't exist";
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event);
  //      if (eu == null)
    //        return "User is not in that event";
        UserCard uc = new UserCard();
        uc.setEventUser(eu);
        uc.setProduct(product);
        return  userCardRepository.save(uc);
    }
    @Override
    public List<Product> findAllEventProducts(Integer id) {
        List<Product> products = new ArrayList<>();
        Event event = eventRepository.findById(id).orElse(null);
        if (event==null)
            return null;
        for (EventUser eu : event.getEventUsers())
            for (UserCard uc: eu.getUserCard())
                products.add(uc.getProduct());
        return products;
    }
    @Override
    public String deleteProduct(Integer id, Integer productToDeleteId, Long userId) {
        Event event = eventRepository.findById(id).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        EventUser event_user = eventUserRepository.findByUserAndEvent(user,event);
        if (event==null)
            return "Event doesn't exist";
        if (user==null)
            return "Unexpected failure";
        if (event_user == null)
            return "User is not in that event";
        List<UserCard> userCards = userCardRepository.findAllByEventUser(event_user);
        for (UserCard uc : userCards){
            Product product = uc.getProduct();
            if (product.getId() == productToDeleteId )
                userCardRepository.delete(uc);
        }
        return "Successfully delete products";

    }
    @Override
    public List<Product> findUserEventProducts(Integer event_id, Long user_id) {
        List<Product> userEventProducts = new ArrayList<>();
        User user = userRepository.findById(user_id).orElse(null);
        if (user == null)
            return null;
        Event event = eventRepository.findById(event_id).orElse(null);
        if (event == null)
            return null;
        EventUser eu = eventUserRepository.findByUserAndEvent(user,event);
        if (eu == null )
            return null;
        List<UserCard> uc = userCardRepository.findAllByEventUser(eu);
        for (UserCard u : uc){
            userEventProducts.add(u.getProduct());
        }
        return userEventProducts;
    }
}
