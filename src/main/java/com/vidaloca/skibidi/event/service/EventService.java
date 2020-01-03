package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.model.User;

public interface EventService {
    Event addNewEvent(EventDto eventDto, Long currentUserId);
    Event updateEvent(EventDto eventDto, Integer id);
    Event addProductToEvent(Product product, Integer eventId);
    Event addUserToEvent(User user, Integer eventId);
}
