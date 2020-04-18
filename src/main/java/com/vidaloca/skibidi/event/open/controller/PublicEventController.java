package com.vidaloca.skibidi.event.open.controller;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.open.service.PublicEventService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PublicEventController {

    private PublicEventService publicEventService;

    @Autowired
    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @PostMapping("/event/{eventId}/join")
    public EventUser joinEvent(@PathVariable Long eventId, HttpServletRequest request){
        return publicEventService.joinEvent(CurrentUser.currentUserId(request),eventId);
    }

    @GetMapping("/event/public")
    public List<Event> getAllPublicEvents(){
        return publicEventService.findAllPublicEvents();
    }

}
