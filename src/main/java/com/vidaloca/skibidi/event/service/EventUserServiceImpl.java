package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.access.repository.EventInvitationRepository;
import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.LastAdminException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.event.exception.model.UserIsNotInEventException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventUserServiceImpl implements EventUserService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private EventInvitationRepository eventInvitationRepository;

    public EventUserServiceImpl(UserRepository userRepository, EventRepository eventRepository,
                                EventUserRepository eventUserRepository, EventInvitationRepository eventInvitationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
        this.eventInvitationRepository = eventInvitationRepository;
    }

    @Override
    public List<User> findAllEventUsers(Long eventId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        List<EventUser> eventUsers = eventUserRepository.findAllByEvent(event);
        Optional<EventUser> eu = eventUserRepository.findByUserAndEvent(user,event);
        if (event.getEventType()!= EventType.PUBLIC && eu.isEmpty() )
            return null;
        return eventUsers.stream().map(EventUser::getUser).collect(Collectors.toList());
    }

    //Only FOR ADMIN
    @Override
    public String deleteUser(Long eventId, Long userToDeleteId, Long userId) throws UserIsNotAdminException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User userToDelete = userRepository.findById(userToDeleteId).orElseThrow(() -> new UserNotFoundException(userToDeleteId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        if (!eu.isAdmin())
            throw new UserIsNotAdminException(userId);
        EventUser eu2 = eventUserRepository.findByUserAndEvent(userToDelete, event).orElseThrow(() -> new UserIsNotInEventException(userToDelete.getId(), event.getId()));
        eventUserRepository.delete(eu2);
        Optional<EventInvitation> eventInvitation = eventInvitationRepository.findByUser(user);
        eventInvitation.ifPresent(invitation -> eventInvitationRepository.delete(invitation));
        return "Successfully removed user from event";
    }

    @Override
    public String grantUserAdmin(Long eventId, Long userToGrantId, Long userId) throws UserIsNotAdminException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User userToGrant = userRepository.findById(userToGrantId).orElseThrow(() -> new UserNotFoundException(userToGrantId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        if (!eu.isAdmin())
            throw new UserIsNotAdminException(userId);
        EventUser eu2 = eventUserRepository.findByUserAndEvent(userToGrant, event).orElseThrow(() -> new UserIsNotInEventException(userToGrant.getId(), event.getId()));
        eu2.setAdmin(true);
        eventUserRepository.save(eu2);
        return "Successfully granted admin to " + userToGrant.getUsername();

    }

    @Override
    public boolean isCurrentUserAdminOfEvent(Long eventId , Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return (eu.isAdmin());
    }

    @Override
    public boolean leaveEvent(Long eventId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        long counter = event.getEventUsers().stream().filter(EventUser::isAdmin).count();
        if (eu.isAdmin() && counter < 2 )
            throw new LastAdminException();
        event.getEventUsers().removeIf(eventUser -> eventUser.getUser().equals(user));
        eventRepository.save(event);
        eventUserRepository.delete(eu);
        Optional<EventInvitation> eventInvitation = eventInvitationRepository.findByUser(user);
        eventInvitation.ifPresent(invitation -> eventInvitationRepository.delete(invitation));
        return true;
    }

    @Override
    public List<User> findAllEventAdmins(Long eventId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eu = eventUserRepository.findByUserAndEvent(user, event).orElseThrow(() -> new UserIsNotInEventException(user.getId(), event.getId()));
        return event.getEventUsers().stream().filter(EventUser::isAdmin).map(EventUser::getUser).collect(Collectors.toList());
    }


}
