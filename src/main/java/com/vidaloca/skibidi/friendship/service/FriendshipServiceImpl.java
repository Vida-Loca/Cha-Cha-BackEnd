package com.vidaloca.skibidi.friendship.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.event.type.EventType;
import com.vidaloca.skibidi.friendship.exception.InvitationExistsException;
import com.vidaloca.skibidi.friendship.exception.InvitationNotFoundException;
import com.vidaloca.skibidi.friendship.exception.RelationNotFoundException;
import com.vidaloca.skibidi.friendship.exception.UserNotAllowedException;
import com.vidaloca.skibidi.friendship.model.Invitation;
import com.vidaloca.skibidi.friendship.model.Relation;
import com.vidaloca.skibidi.friendship.repository.InvitationRepository;
import com.vidaloca.skibidi.friendship.repository.RelationRepository;
import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.friendship.status.RelationStatus;
import com.vidaloca.skibidi.user.exception.UserNotFoundException;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    private UserRepository userRepository;
    private InvitationRepository invitationRepository;
    private RelationRepository relationRepository;

    @Autowired
    public FriendshipServiceImpl(UserRepository userRepository, InvitationRepository invitationRepository, RelationRepository relationRepository) {
        this.userRepository = userRepository;
        this.invitationRepository = invitationRepository;
        this.relationRepository = relationRepository;
    }

    @Override
    public List<User> findAllByUsernameContains(String regex) {
        return userRepository.findAllByUsernameContains(regex);
    }

    @Override
    public List<User> findAllUserFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Relation> relations = user.getRelations();
        List<User> friends = new ArrayList<>();
        for (Relation r: relations) {
            if (r.getRelationStatus() == RelationStatus.FRIENDS)
                friends.add(userRepository.findById(r.getRelatedUserId()).orElseThrow(()-> new UserNotFoundException(r.getRelatedUserId())));
        }
        return friends;
    }

    @Override
    public Set<Event> findAllFriendsEvents(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<User> friends = findAllUserFriends(userId);
        List<EventUser> eventUserList = new ArrayList<>();
        friends.forEach(u -> eventUserList.addAll(new ArrayList<EventUser>(u.getEventUsers())));
        return eventUserList.stream().filter(EventUser::isAdmin).filter(eu-> eu.getEvent().getEventType().isVisible()).
                filter(eu -> !eu.getEvent().isOver()).
                filter(eu -> eu.getUser() != user ).map(EventUser::getEvent).collect(Collectors.toSet());

    }

    @Override
    public List<Invitation> findAllUserInvitations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getInvitationsToUser().stream().filter(f -> f.getInvitationStatus().equals(InvitationStatus.PROCESSING))
                .collect(Collectors.toList());
    }

    @Override
    public Invitation inviteFriend(Long invitorId, Long invitedId){
        User invitor = userRepository.findById(invitorId).orElseThrow(() -> new UserNotFoundException(invitorId));
        User invited = userRepository.findById(invitedId).orElseThrow(() -> new UserNotFoundException(invitedId));
        Optional<Relation> relation = relationRepository.findByUserAndRelatedUserId(invitor, invited.getId());
        if (relation.isPresent() && !relation.get().getRelationStatus().equals(RelationStatus.REMOVED))
            throw new UserNotAllowedException(invitorId, "invite");
        Optional<Invitation> invitation = invitationRepository.findByInvitorAndInvited(invitor, invited);
        if (invitation.isPresent()) {
            if (invitation.get().getInvitationStatus().equals(InvitationStatus.ACCEPTED) ||
                    invitation.get().getInvitationStatus().equals(InvitationStatus.PROCESSING))
                throw new InvitationExistsException(invitorId, invitedId, invitation.get().getInvitationStatus());
            else {
                invitation.get().setInvitationStatus(InvitationStatus.PROCESSING);
                return invitationRepository.save(invitation.get());
            }
        }
        Invitation invitation1 = Invitation.builder().invitor(invitor).invited(invited).build();
        return invitationRepository.save(invitation1);
    }

    @Override
    public Invitation cancelInvitation(Long invitationId, Long invitorId)  {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new InvitationNotFoundException(invitationId));
        if (!invitation.getInvitor().getId().equals(invitorId))
            throw new UserNotAllowedException(invitorId, "cancel");
        invitation.setInvitationStatus(InvitationStatus.CANCELLED);
        return invitationRepository.save(invitation);
    }

    @Override
    public Relation acceptInvitation(Long invitationId, Long invitedId)  {
        User invited = userRepository.findById(invitedId).orElseThrow(() -> new UserNotFoundException(invitedId));
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new InvitationNotFoundException(invitationId));
        if (invitation.getInvitor().getId().equals(invitedId))
            throw new UserNotAllowedException(invitedId, "accept");
        invitation.setInvitationStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
        Optional<Relation> relation = relationRepository.findByUserAndRelatedUserId(invitation.getInvitor(),invitation.getInvited().getId());
        Optional<Relation> relation2 = relationRepository.findByUserAndRelatedUserId(invitation.getInvited(),invitation.getInvitor().getId());
        if (relation.isPresent() && relation2.isPresent()){
            Relation rel= relation.get();
            rel.setRelationStatus(RelationStatus.FRIENDS);
            relationRepository.save(rel);
            Relation rel2 = relation2.get();
            rel2.setRelationStatus(RelationStatus.FRIENDS);
            return relationRepository.save(rel2);
        }
        Relation r1 = Relation.builder().user(invitation.getInvitor()).
                relatedUserId(invited.getId()).relationStatus(RelationStatus.FRIENDS).build();
        relationRepository.save(r1);
        Relation r2= Relation.builder().user(invited).relatedUserId(invitation.getInvitor().getId()).
                relationStatus(RelationStatus.FRIENDS).build();
        return relationRepository.save(r2);
    }

    @Override
    public Invitation rejectInvitation(Long invitationId, Long invitedId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new InvitationNotFoundException(invitationId));
        if (invitation.getInvitor().getId().equals(invitedId))
            throw new UserNotAllowedException(invitedId, "reject");
        invitation.setInvitationStatus(InvitationStatus.REJECTED);
        return invitationRepository.save(invitation);
    }

    @Override
    public Relation removeFriend(Long userId, Long userToRemoveId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Relation relation = relationRepository.findByUserAndRelatedUserId(user, userToRemoveId).orElseThrow(()-> new RelationNotFoundException(userId,userToRemoveId));
        if (relation.getRelationStatus() != RelationStatus.FRIENDS)
            throw new UserNotAllowedException(user.getId(), "remove");
        User userToRemove = userRepository.findById(userToRemoveId).orElseThrow(() -> new UserNotFoundException(userToRemoveId));
        Invitation invitation = invitationRepository.findByInvitorAndInvited(user,userToRemove).orElse(
                invitationRepository.findByInvitorAndInvited(userToRemove,user).orElse(null)
        );
        if (invitation != null)
            invitationRepository.delete(invitation);
        relation.setRelationStatus(RelationStatus.REMOVED);
        Relation relation2 = relationRepository.findByUserAndRelatedUserId(userToRemove, user.getId()).orElseThrow(()-> new RelationNotFoundException(userId,userToRemoveId));
        relation2.setRelationStatus(RelationStatus.REMOVED);
        relationRepository.save(relation2);
        return relationRepository.save(relation);
    }

    @Override
    public Relation blockUser(Long userId, Long userToBlockId)  {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Relation relation = relationRepository.findByUserAndRelatedUserId(user, userToBlockId).orElseThrow(()-> new RelationNotFoundException(userId,userToBlockId));
        if (relation.getRelationStatus() == RelationStatus.BLOCKED)
            throw new UserNotAllowedException(user.getId(), "block");
        relation.setRelationStatus(RelationStatus.BLOCKED);
        Relation relation2 = relationRepository.findByUserAndRelatedUserId(userRepository.findById(userToBlockId).orElseThrow(() -> new UserNotFoundException(userToBlockId)), user.getId()).orElseThrow(()-> new RelationNotFoundException(userId,userToBlockId));
        relation2.setRelationStatus(RelationStatus.BLOCKED);
        relationRepository.save(relation2);
        return relationRepository.save(relation);
    }
}
