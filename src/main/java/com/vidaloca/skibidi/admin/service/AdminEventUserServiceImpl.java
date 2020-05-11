package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//JAN LEWANDOWSKI COPYRIGHT

@Service
public class AdminEventUserServiceImpl implements AdminEventUserService {

    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private UserRepository userRepository;

    @Autowired
    public AdminEventUserServiceImpl(EventRepository eventRepository, EventUserRepository eventUserRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllEventUsers(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        List<EventUser> eventUsers = eventUserRepository.findAllByEvent(event);
        return eventUsers.stream().map(EventUser::getUser).collect(Collectors.toList());
    }

    @Override
    public String deleteUserFromEvent(Long userToDeleteId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User userToDelete = userRepository.findById(userToDeleteId).orElseThrow(() -> new UserNotFoundException(userToDeleteId));
        EventUser eu = eventUserRepository.findByUserAndEvent(userToDelete, event).orElseThrow(() -> new UserIsNotInEventException(userToDelete.getId(), event.getId()));
        eventUserRepository.delete(eu);
        return "Successfully removed user from event";
    }

    // Function below returns actuall isAdmin status after change by admin
    @Override
    public boolean grantTakeUserEventAdmin(Long eventId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        eu.setAdmin(!eu.isAdmin());
        eventUserRepository.save(eu);
        return eu.isAdmin();
    }
}
