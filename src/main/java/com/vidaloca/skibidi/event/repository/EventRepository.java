package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,Integer> {
}