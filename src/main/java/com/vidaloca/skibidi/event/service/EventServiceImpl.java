package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.repository.UserCardRepository;
import com.vidaloca.skibidi.model.*;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private EventUserRepository event_userRepository;
    private UserCardRepository userCardRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            EventUserRepository event_userRepository, UserCardRepository userCardRepository,
                            ProductRepository productRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.event_userRepository = event_userRepository;
        this.userCardRepository = userCardRepository;
    }

    @Override
    public String addNewEvent(EventDto eventDto, Long currentUserId) {
        Event event = new Event();
        User user = userRepository.findById(currentUserId).orElse(null);
        event.setAddress(new Address());
        Event finalEvent = getEvent(eventDto, event);
        eventRepository.save(finalEvent);
        EventUser eu = new EventUser(user, event, true);
        event_userRepository.save(eu);
        return "Successfully added event";
    }

    @Override
    public String updateEvent(EventDto eventDto, Integer id, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Event event = eventRepository.findById(id).orElse(null);
        if (user == null)
            return "Unexpected failure";
        if (event == null)
            return "Event doesn't exist";
        EventUser eu = event_userRepository.findByUserAndEvent(user, event);
        if (eu == null)
            return "User is not in that event";
        if (!eu.isAdmin())
            return "User is not allowed to update event";
        eventRepository.save(getEvent(eventDto, event));
        return "Updated successfully";
    }

    @Override
    public String addProductToEvent(Product product, Integer eventId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return "Unexpected failure";
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return "Event doesn't exist";
        EventUser eu = event_userRepository.findByUserAndEvent(user, event);
        UserCard uc = new UserCard();
        uc.setEventUser(eu);
        uc.setProduct(product);
        userCardRepository.save(uc);
        event.getProducts().add(product);
        eventRepository.save(event);
        return "Successfully added product";
    }

    @Override
    public String addUserToEvent(String username, Integer eventId, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            return "Unexpected failure";
        User addingUser = userRepository.findByUsername(username);
        if (addingUser == null)
            return "User doesn't exists";
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return "Event doesn't exist";
        EventUser eu = event_userRepository.findByUserAndEvent(user, event);
        if (eu == null)
            return "User is not in that event";
        EventUser eu2 = event_userRepository.findByUserAndEvent(addingUser, event);
        if (eu2 != null)
            return  "User is acctually in that event";
        if (!eu.isAdmin())
            return "User don't have permission to add new user to event";
        EventUser euAdd = new EventUser(addingUser, event, false);
        event_userRepository.save(euAdd);
        return "Successfully added user";
    }

    @Override
    public String deleteEvent(Integer id, Long user_id) {
        User user = userRepository.findById(user_id).orElse(null);
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return "Event doesn't exist";
        EventUser eu = event_userRepository.findByUserAndEvent(user, event);
        if (eu == null)
            return "User is not in that event";
        if (!eu.isAdmin())
            return "User don't have permission to delete event";
        eventRepository.deleteById(id);
        return "Event delete successfully";
    }

    @Override
    public List<User> findAllUsers(Integer event_id) {
        Event event = eventRepository.findById(event_id).orElse(null);
        if (event==null)
            return null;
        List<EventUser> event_users = event_userRepository.findAllByEvent(event);
        List <User> users = new ArrayList<>();
        for (EventUser eu: event_users)
            users.add(eu.getUser());
        return users;
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
        EventUser eu = event_userRepository.findByUserAndEvent(user,event);
        if (eu == null )
            return null;
        List<UserCard> uc = userCardRepository.findAllByEventUser(eu);
        for (UserCard u : uc){
            userEventProducts.add(u.getProduct());
        }
        return userEventProducts;
    }

    @Override
    public String deleteUser(Integer id,Long userToDeleteId, Long userId) {
        Event event = eventRepository.findById(id).orElse(null);
        User userToDelete = userRepository.findById(userToDeleteId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        EventUser event_user = event_userRepository.findByUserAndEvent(user,event);
        if (event==null)
            return "Event doesn't exist";
        if (user==null)
            return "Unexpected failure";
        if (userToDelete==null)
            return "User to delete doesn't exist";
        EventUser eu = event_userRepository.findByUserAndEvent(user, event);
        if (eu == null)
            return "User is not in that event";
        EventUser eu2 = event_userRepository.findByUserAndEvent(userToDelete, event);
        if (eu2 == null)
            return  "User is not acctually in that event";
        if (!event_user.isAdmin())
            return "You don't have permission to delete user";
        event_userRepository.deleteById(eu.getId());
        return "Successfully removed user from event";

    }

    @Override
    public String deleteProduct(Integer id, Integer productToDeleteId, Long userId) {
        Event event = eventRepository.findById(id).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        EventUser event_user = event_userRepository.findByUserAndEvent(user,event);
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
        event.getProducts().removeIf(p -> p.getId() == productToDeleteId);
        return "Successfully delete products";

    }

    @Override
    public String grantUserAdmin(Integer event_id, Long userToGrantId, Long user_id) {
        Event event = eventRepository.findById(event_id).orElse(null);
        User userToGrant = userRepository.findById(userToGrantId).orElse(null);
        User user = userRepository.findById(user_id).orElse(null);
        EventUser event_user = event_userRepository.findByUserAndEvent(user,event);
        if (event==null)
            return "Event doesn't exist";
        if (user==null)
            return "Unexpected failure";
        if (userToGrant==null)
            return "User to grant doesn't exist";
        EventUser eu = event_userRepository.findByUserAndEvent(user, event);
        if (eu == null)
            return "You are not in that event";
        EventUser eu2 = event_userRepository.findByUserAndEvent(userToGrant, event);
        if (eu2 == null)
            return  "User is not acctually in that event";
        if (!event_user.isAdmin())
            return "You don't have permission to grant admin to user";
        eu2.setAdmin(true);
        event_userRepository.save(eu2);
        return "Successfully granted admin to " + user.getUsername();

    }

    private Event getEvent(EventDto eventDto, Event event) {
        event.setName(eventDto.getName());
        event.setStartDate(eventDto.getStartDate());
        event.getAddress().setCity(eventDto.getAddress().getCity());
        event.getAddress().setCountry(eventDto.getAddress().getCountry());
        event.getAddress().setNumber(eventDto.getAddress().getNumber());
        event.getAddress().setStreet(eventDto.getAddress().getStreet());
        event.getAddress().setPostcode(eventDto.getAddress().getPostcode());
        return event;
    }
}
