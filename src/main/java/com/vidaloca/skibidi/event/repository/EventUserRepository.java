package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventUserRepository extends CrudRepository<EventUser,Long> {
    Optional<EventUser> findByUserAndEvent(User user, Event event);
    List<EventUser> findAllByEvent(Event event);
    List<EventUser> findAllByEvent_Id(Long eventId);
    List<EventUser> findAllByUser (User user);
}
