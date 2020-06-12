package com.vidaloca.skibidi.event.access.repository;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EventInvitationRepository extends CrudRepository<EventInvitation,Long> {
    Optional<EventInvitation> findByUser(User user);
    Optional<EventInvitation> findByUserAndEvent(User user, Event event);
}
