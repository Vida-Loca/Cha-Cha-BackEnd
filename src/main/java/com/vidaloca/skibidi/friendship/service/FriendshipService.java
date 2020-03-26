package com.vidaloca.skibidi.friendship.service;

import com.vidaloca.skibidi.friendship.model.Friendship;
import com.vidaloca.skibidi.user.model.User;

import java.util.List;

public interface FriendshipService {
    List<User> findAllUserFriends(Long userId);

    List<Friendship> findAllUserInvitations(Long userId);

    Friendship inviteFriend(Long invitorId, Long invitedId);

    Friendship cancelInvitation(Long friendshipId, Long invitorId);

    Friendship acceptInvitation(Long friendshipId, Long invitedId);

    Friendship rejectInvitation(Long friendshipId, Long invitedId);

    Friendship removeFriend(Long userId, Long userToRemoveId);
}
