package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Event_User;
import com.vidaloca.skibidi.model.User;
import org.springframework.data.repository.CrudRepository;

public interface Event_UserRepository extends CrudRepository<Event_User,Integer> {
    Event_User findByUserAndEvent(User user, Event event);
}
