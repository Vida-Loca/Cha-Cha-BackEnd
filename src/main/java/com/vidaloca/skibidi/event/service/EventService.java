package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.model.Event;

import java.util.List;

public interface EventService {

    Event findById(Long id);

    List<Event> findAllEvents();

    Event addNewEvent(EventDto eventDto, Long currentUserId);

    Event updateEvent(EventDto eventDto, Long eventId, Long userId) throws UserIsNotAdminException;

    String deleteEvent(Long id, Long user_id) throws UserIsNotAdminException;

}
