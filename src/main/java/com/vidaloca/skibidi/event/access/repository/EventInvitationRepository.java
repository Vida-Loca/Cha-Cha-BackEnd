package com.vidaloca.skibidi.event.access.repository;

import com.vidaloca.skibidi.event.access.model.EventInvitation;
import org.springframework.data.repository.CrudRepository;

public interface EventInvitationRepository extends CrudRepository<EventInvitation,Long> {
}
