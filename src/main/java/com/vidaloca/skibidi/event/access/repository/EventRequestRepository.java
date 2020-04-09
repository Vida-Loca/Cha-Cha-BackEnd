package com.vidaloca.skibidi.event.access.repository;

import com.vidaloca.skibidi.event.access.model.EventRequest;
import org.springframework.data.repository.CrudRepository;

public interface EventRequestRepository extends CrudRepository<EventRequest,Long> {
}
