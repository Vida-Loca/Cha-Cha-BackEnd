package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventUserRepository extends CrudRepository<EventUser,Integer> {
    EventUser findByUserAndEvent(User user, Event event);
    List<EventUser> findAllByEvent(Event event);
    List<EventUser> findAllByUser (User user);
}
