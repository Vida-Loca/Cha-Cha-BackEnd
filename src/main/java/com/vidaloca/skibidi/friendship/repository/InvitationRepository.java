package com.vidaloca.skibidi.friendship.repository;

import com.vidaloca.skibidi.friendship.model.Invitation;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InvitationRepository extends CrudRepository<Invitation,Long> {
    Optional<Invitation> findByInvitorAndInvited(User invitor, User invited);
}
