package com.vidaloca.skibidi.event.access.controller;

import com.vidaloca.skibidi.event.access.model.EventRequest;
import com.vidaloca.skibidi.event.access.service.RequestService;
import com.vidaloca.skibidi.event.exception.model.UserActuallyInEventException;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RequestController {

    private RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/event/{eventId}/requests")
    public List<EventRequest> getAllEventRequests(@PathVariable Long eventId, HttpServletRequest request){
        return requestService.showAllEventRequest(CurrentUser.currentUserId(request),eventId);
    }

    @GetMapping("/user/event_requests")
    public List<EventRequest> getAllUserEventRequests(HttpServletRequest request){
        return requestService.showAllUserRequest(CurrentUser.currentUserId(request));
    }

    @PostMapping("/event/{eventId}/send_request")
    public EventRequest sendRequest(@PathVariable Long eventId, HttpServletRequest request) throws UserActuallyInEventException {
        return requestService.sendRequestToEvent(CurrentUser.currentUserId(request),eventId);
    }

    @PutMapping("/event/request/{requestId}/accept")
    public EventRequest acceptRequest(@PathVariable Long requestId, HttpServletRequest request){
        return requestService.acceptRequest(requestId,CurrentUser.currentUserId(request));
    }

    @PutMapping("/event/request/{requestId}/reject")
    public EventRequest rejectRequest(@PathVariable Long requestId, HttpServletRequest request){
        return requestService.rejectRequest(requestId,CurrentUser.currentUserId(request));
    }
}
