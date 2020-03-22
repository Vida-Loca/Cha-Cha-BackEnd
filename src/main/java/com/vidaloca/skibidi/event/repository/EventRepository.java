package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.event.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,Long> {
}
