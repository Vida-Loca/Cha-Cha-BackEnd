package com.vidaloca.skibidi.event.open.service;

import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.exception.UsernameNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicEventServiceImpl implements PublicEventService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;

    @Autowired
    public PublicEventServiceImpl(UserRepository userRepository, EventRepository eventRepository, EventUserRepository eventUserRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
    }

    @Override
    public EventUser joinEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getEventType().isPermissionNeeded())
            throw new UserNotAllowedException(userId,"join that event");
        EventUser eu = EventUser.builder().event(event).user(user).build();
        return eventUserRepository.save(eu);
    }

    @Override
    public List<Event> findAllPublicEvents() {
        return eventRepository.findAllByEventType(EventType.PUBLIC);
    }
}
