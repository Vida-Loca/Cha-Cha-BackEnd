package com.vidaloca.skibidi.event.access.controller;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.access.service.InvitationService;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class InvitationController {

    private InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/event/{eventId}/invitations")
    public List<EventInvitation> getAllInvitations(@PathVariable Long eventId, HttpServletRequest request){
        return invitationService.showAllEventInvitations(CurrentUser.currentUserId(request),eventId);
    }

    @GetMapping("/user/event_invitations")
    public List<EventInvitation> getAllUserInvitations(HttpServletRequest request){
        return invitationService.showAllUserInvitations(CurrentUser.currentUserId(request));
    }

    @PostMapping("/event/{eventId}/invite")
    public EventInvitation inviteUser(@PathVariable Long eventId, @RequestParam Long userId, HttpServletRequest request) throws UserActuallyInEventException {
        return invitationService.inviteToEvent(eventId,userId, CurrentUser.currentUserId(request));
    }

    @PutMapping("/event/invite/{invitationId}/accept")
    public EventInvitation acceptInvitation(@PathVariable Long invitationId, HttpServletRequest request){
        return invitationService.acceptInvitation(invitationId,CurrentUser.currentUserId(request));
    }
    @PutMapping("/event/invite/{invitationId}/reject")
    public EventInvitation rejectInvitation(@PathVariable Long invitationId, HttpServletRequest request){
        return invitationService.rejectInvitation(invitationId,CurrentUser.currentUserId(request));
    }

/*    @DeleteMapping("/event/invite/{invitationId}/cancel")
    public void cancelInvitation(@PathVariable Long invitationId, HttpServletRequest request){
      invitationService.cancelInvitation(invitationId,CurrentUser.currentUserId(request));
    }*/

}
