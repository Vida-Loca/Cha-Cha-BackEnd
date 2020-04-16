package com.vidaloca.skibidi.event.open.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;

import java.util.List;

public interface PublicEventService {
    EventUser joinEvent(Long currentUserId, Long eventId);

    List<Event> findAllPublicEvents();

}
