package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.type.EventType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event,Long> {
    List<Event> findAllByEventType(EventType eventType);
}
