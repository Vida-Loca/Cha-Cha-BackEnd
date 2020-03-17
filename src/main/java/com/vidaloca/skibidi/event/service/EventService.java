package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.exception.handler.UserActuallyInEventException;
import com.vidaloca.skibidi.event.exception.handler.UserIsNotAdminException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.user.model.User;

import java.util.List;

public interface EventService {
    Event addNewEvent(EventDto eventDto, Long currentUserId);
    Event updateEvent(EventDto eventDto, Long eventId, Long userId) throws UserIsNotAdminException;
    EventUser addUserToEvent(String username, Long eventId, Long userId) throws UserIsNotAdminException, UserActuallyInEventException;
    String deleteEvent(Long id, Long user_id) throws UserIsNotAdminException;
    List<User> findAllEventUsers(Long eventId);
    String deleteUser(Long eventId,Long userToDeleteId, Long userId) throws UserIsNotAdminException;
    String grantUserAdmin(Long eventId, Long userToGrantId, Long user_id) throws UserIsNotAdminException;
}
