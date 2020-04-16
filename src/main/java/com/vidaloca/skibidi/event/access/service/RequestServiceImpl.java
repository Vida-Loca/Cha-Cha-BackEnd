package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.exception.IsNotProcessingException;
import com.vidaloca.skibidi.event.access.exception.RequestNotFoundException;
import com.vidaloca.skibidi.event.access.exception.UserCantAcceptRequestException;
import com.vidaloca.skibidi.event.access.exception.UserCantRequestToEventException;
import com.vidaloca.skibidi.event.access.model.EventRequest;
import com.vidaloca.skibidi.event.access.repository.EventRequestRepository;
import com.vidaloca.skibidi.event.access.status.AccessStatus;
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

@Service
public class RequestServiceImpl implements RequestService {

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private EventUserRepository eventUserRepository;
    private EventRequestRepository eventRequestRepository;

    @Autowired
    public RequestServiceImpl(UserRepository userRepository, EventRepository eventRepository, EventUserRepository eventUserRepository, EventRequestRepository eventRequestRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventUserRepository = eventUserRepository;
        this.eventRequestRepository = eventRequestRepository;
    }

    @Override
    public EventRequest sendRequestToEvent(Long currentUserId, Long eventId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (!event.getEventType().canUserSendRequest())
            throw new UserCantRequestToEventException(currentUserId);
        EventRequest eventRequest = EventRequest.builder().event(event).user(user).build();
        return eventRequestRepository.save(eventRequest);
    }

    @Override
    public List<EventRequest> showAllEventRequest(Long currentUserId, Long eventId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        EventUser eventUser = eventUserRepository.findByUserAndEvent(user,event).orElseThrow(()-> new UserIsNotInEventException(currentUserId,eventId));
        if (eventUser.isAdmin() || event.getEventType().canUserAcceptRequest())
            return event.getEventRequests().stream().filter(er -> er.getAccessStatus().
                    equals(AccessStatus.PROCESSING)).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<EventRequest> showAllUserRequest(Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        return user.getEventRequests();
    }

    //REJECT REQUEST CAN ONLY ADMIN

    @Override
    public EventRequest rejectRequest(Long requestId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        EventRequest request = eventRequestRepository.findById(requestId).orElseThrow(()-> new RequestNotFoundException(requestId));
        Event event = request.getEvent();
        EventUser eventUser = eventUserRepository.findByUserAndEvent(user,event).orElseThrow(()-> new UserIsNotInEventException(currentUserId,event.getId()));
        if (!eventUser.isAdmin())
            throw new UserCantAcceptRequestException(currentUserId);
        if (!request.getAccessStatus().equals(AccessStatus.PROCESSING))
            throw new IsNotProcessingException("Request", requestId);
        request.setAccessStatus(AccessStatus.REJECTED);
        return eventRequestRepository.save(request);
    }


    @Override
    public EventRequest acceptRequest(Long requestId, Long currentUserId) {
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new UserNotFoundException(currentUserId));
        EventRequest request = eventRequestRepository.findById(requestId).orElseThrow(()-> new RequestNotFoundException(requestId));
        Event event = request.getEvent();
        EventUser eventUser = eventUserRepository.findByUserAndEvent(user,event).orElseThrow(()-> new UserIsNotInEventException(currentUserId,event.getId()));
        if (!eventUser.isAdmin() && !event.getEventType().canUserAcceptRequest())
            throw new UserCantAcceptRequestException(currentUserId);
        if (!request.getAccessStatus().equals(AccessStatus.PROCESSING))
            throw new IsNotProcessingException("Request", requestId);
        request.setAccessStatus(AccessStatus.ACCEPTED);
        EventUser eventUser2 = EventUser.builder().user(request.getUser()).event(event).build();
        eventUserRepository.save(eventUser2);
        return eventRequestRepository.save(request);
    }
}
