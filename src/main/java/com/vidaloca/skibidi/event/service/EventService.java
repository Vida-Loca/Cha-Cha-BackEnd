package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.User;

public interface EventService {
    String addNewEvent(EventDto eventDto, Long currentUserId);
    String updateEvent(EventDto eventDto, Integer id,Long userId);
    String addProductToEvent(Product product, Integer eventId, Long userId);
    String addUserToEvent(String username, Integer eventId,Long userId);
    String deleteEvent(Integer id, Long user_id);
}
