package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.model.Event;

public interface EventService {
    Event addNewEvent(EventDto eventDto);
    Event updateEvent(EventDto eventDto, Integer id);
}
