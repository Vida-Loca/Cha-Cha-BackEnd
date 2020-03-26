package com.vidaloca.skibidi.friendship.repository;

import com.vidaloca.skibidi.friendship.model.Friendship;
import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends CrudRepository<Friendship,Long> {
    Optional<Friendship> findByInvitorAndInvited(User invitor, User invited);
}
