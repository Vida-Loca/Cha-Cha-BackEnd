package com.vidaloca.skibidi.event.access.service;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Override
    public EventInvitation inviteToEvent(Long eventId, Long userId, Long currentUserId) {
        return null;
    }

    @Override
    public List<EventInvitation> showAllUserInvitations(Long currentUserId) {
        return null;
    }

    @Override
    public List<EventInvitation> showAllEventInvitations(Long currentUserId, Long eventId) {
        return null;
    }

    @Override
    public EventInvitation acceptInvitation(Long invitationId, Long currentUserId) {
        return null;
    }

    @Override
    public EventInvitation rejectInvitation(Long invitationId, Long currentUserId) {
        return null;
    }
}
