package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.user.model.User;

import java.util.List;

public interface EventService {
    String addNewEvent(EventDto eventDto, Long currentUserId);
    String updateEvent(EventDto eventDto, Integer id,Long userId);
    String addProductToEvent(Product product, Integer eventId, Long userId);
    String addUserToEvent(String username, Integer eventId,Long userId);
    String deleteEvent(Integer id, Long user_id);
    List<Product> findAllEventProducts(Integer id);
    List<User> findAllUsers(Integer event_id);
    List<Product> findUserEventProducts(Integer event_id, Long user_id);
    String deleteUser(Integer id,Long userToDeleteId, Long userId);
    String deleteProduct(Integer id,Integer productToDeleteId, Long userId);
    String grantUserAdmin(Integer event_id, Long userToGrantId, Long user_id);
}
