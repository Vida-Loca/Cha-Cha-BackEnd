package com.vidaloca.skibidi.friendship.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.friendship.exception.InvitationExistsException;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import com.vidaloca.skibidi.friendship.model.Invitation;
import com.vidaloca.skibidi.friendship.model.Relation;
import com.vidaloca.skibidi.user.model.User;

import java.util.List;
import java.util.Set;

public interface FriendshipService {

    List<User> findAllByUsernameContains(String regex);

    List<User> findAllUserFriends(Long userId);

    Set<Event> findAllFriendsEvents(Long userId);

    List<Invitation> findAllUserInvitations(Long userId);

    Invitation inviteFriend(Long invitorId, Long invitedId) throws InvitationExistsException, UserNotAllowedException;

    Invitation cancelInvitation(Long friendshipId, Long invitorId) throws UserNotAllowedException;

    Relation acceptInvitation(Long friendshipId, Long invitedId) throws UserNotAllowedException;

    Invitation rejectInvitation(Long friendshipId, Long invitedId) throws UserNotAllowedException;

    Relation removeFriend(Long userId, Long userToRemoveId) throws UserNotAllowedException;

    Relation blockUser(Long userId, Long userToBlockId) throws UserNotAllowedException;
}

