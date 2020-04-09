package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.model.EventRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public EventRequest sendRequestToEvent(Long currentUserId, Long eventId) {
        return null;
    }

    @Override
    public List<EventRequest> showAllEventRequest(Long currentUserId, Long eventId) {
        return null;
    }

    @Override
    public List<EventRequest> showAllUserRequest(Long currentUserId) {
        return null;
    }

    @Override
    public EventRequest rejectRequest(Long requestId, Long currentUserId) {
        return null;
    }

    @Override
    public EventRequest acceptRequest(Long requestId, Long currentUserId) {
        return null;
    }
}
