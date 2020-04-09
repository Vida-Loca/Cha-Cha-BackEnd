package com.vidaloca.skibidi.event.access.service;


import com.vidaloca.skibidi.event.access.model.EventRequest;

import java.util.List;

public interface RequestService {

    EventRequest sendRequestToEvent (Long currentUserId, Long eventId);

    List<EventRequest> showAllEventRequest (Long currentUserId, Long eventId);

    List<EventRequest> showAllUserRequest (Long currentUserId);

    EventRequest rejectRequest (Long requestId, Long currentUserId);

    EventRequest acceptRequest (Long requestId, Long currentUserId);
}
