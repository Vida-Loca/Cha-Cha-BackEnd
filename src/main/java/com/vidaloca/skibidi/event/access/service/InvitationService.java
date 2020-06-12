package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;

import java.util.List;

public interface InvitationService {

    EventInvitation inviteToEvent(Long eventId, Long userId, Long currentUserId) throws UserActuallyInEventException;

    List<EventInvitation> showAllUserInvitations(Long currentUserId);

    List<EventInvitation> showAllEventInvitations(Long currentUserId, Long eventId);

    EventInvitation acceptInvitation(Long invitationId, Long currentUserId);

    EventInvitation rejectInvitation(Long invitationId, Long currentUserId);

    void cancelInvitation(Long invitationId, Long currentUserId);


}
