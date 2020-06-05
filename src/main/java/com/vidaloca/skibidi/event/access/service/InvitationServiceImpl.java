package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.exception.InvitationNotFoundException;
import com.vidaloca.skibidi.event.access.exception.UserCantInviteToEventException;
import com.vidaloca.skibidi.event.access.exception.InvitationIsNotProcessingException;
import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.access.repository.EventInvitationRepository;
import com.vidaloca.skibidi.event.access.status.AccessStatus;
import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvitationServiceImpl implements InvitationService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private EventInvitationRepository eventInvitationRepository;
    private EventUserRepository eventUserRepository;

    @Autowired
    public InvitationServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                                 EventInvitationRepository eventInvitationRepository, EventUserRepository eventUserRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventInvitationRepository = eventInvitationRepository;
        this.eventUserRepository = eventUserRepository;
    }


    @Override
    public EventInvitation inviteToEvent(Long eventId, Long userId, Long currentUserId) throws UserActuallyInEventException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        EventUser eu = eventUserRepository.findByUserAndEvent(currentUser,event).orElseThrow(()-> new UserIsNotInEventException(currentUserId,eventId));
        Optional<EventUser> eu2 = eventUserRepository.findByUserAndEvent(user,event);
        if (eu2.isPresent())
            throw new UserActuallyInEventException(user.getUsername());
        if (eu.isAdmin() || eu.getEvent().getEventType().canUserInvite())
            return eventInvitationRepository.save(
                    EventInvitation.builder().event(event).user(user).build());
        throw new UserCantInviteToEventException(currentUserId);
    }

    @Override
    public List<EventInvitation> showAllUserInvitations(Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        return user.getEventInvitations();
    }

    @Override
    public List<EventInvitation> showAllEventInvitations(Long currentUserId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        EventUser eu = eventUserRepository.findByUserAndEvent(currentUser,event).orElseThrow(()-> new UserIsNotInEventException(currentUserId,eventId));
        if (eu.isAdmin() || eu.getEvent().getEventType().canUserInvite())
            return eu.getEvent().getEventInvitations().stream().filter(ei -> ei.getAccessStatus().equals(AccessStatus.PROCESSING)).collect(Collectors.toList());
        return null;
    }

    @Override
    public EventInvitation acceptInvitation(Long invitationId, Long currentUserId) {
        EventInvitation invitation = eventInvitationRepository.findById(invitationId).orElseThrow(
                ()->new InvitationNotFoundException(invitationId));
        User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        if (!invitation.getAccessStatus().equals(AccessStatus.PROCESSING))
            throw new InvitationIsNotProcessingException(invitationId);
        invitation.setAccessStatus(AccessStatus.ACCEPTED);
        eventUserRepository.save(EventUser.builder().event(invitation.getEvent()).user(currentUser).build());
        return eventInvitationRepository.save(invitation);
    }

    @Override
    public EventInvitation rejectInvitation(Long invitationId, Long currentUserId) {
        EventInvitation invitation = eventInvitationRepository.findById(invitationId).orElseThrow(
                ()->new InvitationNotFoundException(invitationId));
        User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        if (!invitation.getAccessStatus().equals(AccessStatus.PROCESSING) || invitation.getUser()!=currentUser)
            throw new InvitationIsNotProcessingException(invitationId);
        invitation.setAccessStatus(AccessStatus.REJECTED);
        return eventInvitationRepository.save(invitation);

    }
}
