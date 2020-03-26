package com.vidaloca.skibidi.friendship.service;

import com.vidaloca.skibidi.friendship.exception.FriendshipExistsException;
import com.vidaloca.skibidi.friendship.exception.FriendshipNotFoundException;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import com.vidaloca.skibidi.friendship.model.Friendship;
import com.vidaloca.skibidi.friendship.repository.FriendshipRepository;
import com.vidaloca.skibidi.friendship.status.Status;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    private UserRepository userRepository;
    private FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipServiceImpl(UserRepository userRepository, FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }


    @Override
    public List<User> findAllByUsernameContains(String regex) {
        return userRepository.findAllByUsernameContains(regex);
    }

    @Override
    public List<User> findAllUserFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        List<User> friends = user.getInvitorFriendships().stream().filter(f -> f.getStatus()
                .equals(Status.ACCEPTED)).map(Friendship::getInvitor).collect(Collectors.toList());
        friends.addAll(user.getInvitedFriendships().stream().filter(f -> f.getStatus()
        .equals(Status.ACCEPTED)).map(Friendship::getInvited).collect(Collectors.toList()));
        return friends;
    }

    @Override
    public List<Friendship> findAllUserInvitations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        return user.getInvitedFriendships().stream().filter(f -> f.getStatus().equals(Status.PROCESSING))
                .collect(Collectors.toList());
    }

    @Override
    public Friendship inviteFriend(Long invitorId, Long invitedId) {
        User invitor = userRepository.findById(invitorId).orElseThrow(()-> new UserNotFoundException(invitorId));
        User invited = userRepository.findById(invitedId).orElseThrow(()-> new UserNotFoundException(invitedId));
        Optional<Friendship> friendship = friendshipRepository.findByInvitorAndInvited(invitor, invited);
        if (friendship.isPresent())
            throw new FriendshipExistsException(invitorId,invitedId,friendship.get().getStatus());
        return Friendship.FriendshipBuilder.aFriendship().withInvitor(invitor).withInvited(invited).
                withInvitationStatus(Status.PROCESSING).build();
    }

    @Override
    public Friendship cancelInvitation(Long friendshipId, Long invitorId) {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new FriendshipNotFoundException(friendshipId));
        if (!friendship.getInvitor().getId().equals(invitorId))
            throw new UserNotAllowedException(invitorId,"cancel");
        friendship.setStatus(Status.CANCELLED);
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship acceptInvitation(Long friendshipId, Long invitedId) {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new FriendshipNotFoundException(friendshipId));
        if (!friendship.getInvitor().getId().equals(invitedId))
            throw new UserNotAllowedException(invitedId,"accept");
        friendship.setStatus(Status.ACCEPTED);
        friendship.setFriendshipStartTime(LocalDateTime.now());
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship rejectInvitation(Long friendshipId, Long invitedId) {
        Friendship friendship = friendshipRepository.findById(friendshipId).orElseThrow(() -> new FriendshipNotFoundException(friendshipId));
        if (!friendship.getInvitor().getId().equals(invitedId))
            throw new UserNotAllowedException(invitedId,"reject");
        friendship.setStatus(Status.REJECTED);
        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship removeFriend(Long userId, Long userToRemoveId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        User userToRemove = userRepository.findById(userToRemoveId).orElseThrow(()-> new UserNotFoundException(userToRemoveId));
        Optional<Friendship> friendship = friendshipRepository.findByInvitorAndInvited(user,userToRemove);
        if (friendship.isPresent()) {
            friendship.get().setStatus(Status.REMOVED);
            return friendshipRepository.save(friendship.get());
        }
        friendship = friendshipRepository.findByInvitorAndInvited(userToRemove,user);
        if (friendship.isPresent()){
            friendship.get().setStatus(Status.REMOVED);
            return friendshipRepository.save(friendship.get());
        }
        throw new FriendshipNotFoundException(userId,userToRemoveId);
    }
}
