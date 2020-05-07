package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.event.model.Event;

import java.util.List;

public interface AdminEventService {

    Event findById(Long id);

    List<Event> findAllEvents();

    String deleteById(Long id);


}
