package com.vidaloca.skibidi.friendship.service;

import com.vidaloca.skibidi.friendship.exception.InvitationExistsException;
import com.vidaloca.skibidi.friendship.exception.InvitationNotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        return relationRepository.findAllByUser(user).stream().filter(r -> r.getRelationStatus().equals(RelationStatus.FRIENDS)).
                map(f -> userRepository.findById(f.getRelatedUserId()).orElseThrow(() -> new UserNotFoundException(f.getRelatedUserId())))
                .collect(Collectors.toList());
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
        Invitation invitation1 = Invitation.InvitationBuilder.anInvitation().withInvitor(invitor).withInvited(invited).
                withInvitationStatus(InvitationStatus.PROCESSING).build();
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
        Relation relation1 = Relation.RelationBuilder.aRelation().withUser(invitation.getInvitor()).
                withRelatedUserId(invited.getId()).withRelationStartDate(LocalDateTime.now()).
                withRelationStatus(RelationStatus.FRIENDS).build();
        relationRepository.save(relation1);
        Relation relation2 = Relation.RelationBuilder.aRelation().withUser(invited).withRelatedUserId(invitation.getInvitor().getId()).
                withRelationStartDate(LocalDateTime.now()).withRelationStatus(RelationStatus.FRIENDS).build();
        return relationRepository.save(relation2);
    }

    @Override
    public Invitation rejectInvitation(Long invitationId, Long invitedId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElseThrow(() -> new InvitationNotFoundException(invitationId));
        if (!invitation.getInvitor().getId().equals(invitedId))
            throw new UserNotAllowedException(invitedId, "reject");
        invitation.setInvitationStatus(InvitationStatus.REJECTED);
        return invitationRepository.save(invitation);
    }

    @Override
    public Relation removeFriend(Long userId, Long userToRemoveId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Optional<Relation> relation = relationRepository.findByUserAndRelatedUserId(user, userToRemoveId);
        if (relation.isEmpty())
            throw new InvitationNotFoundException(userId, userToRemoveId);
        if (relation.get().getRelationStatus() != RelationStatus.FRIENDS)
            throw new UserNotAllowedException(user.getId(), "remove");
        User userToRemove = userRepository.findById(userToRemoveId).orElseThrow(() -> new UserNotFoundException(userToRemoveId));
        Invitation invitation = invitationRepository.findByInvitorAndInvited(user, userToRemove).
                orElse(invitationRepository.findByInvitorAndInvited(userToRemove, user).orElse(null));
        if (invitation != null)
            invitationRepository.delete(invitation);
        relation.get().setRelationStatus(RelationStatus.REMOVED);
        Relation relation2 = relationRepository.findByUserAndRelatedUserId(userRepository.findById(userToRemoveId).orElseThrow(() -> new UserNotFoundException(userToRemoveId)), user.getId()).get();
        relation2.setRelationStatus(RelationStatus.REMOVED);
        relationRepository.save(relation2);
        return relationRepository.save(relation.get());
    }

    @Override
    public Relation blockUser(Long userId, Long userToBlockId)  {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Optional<Relation> relation = relationRepository.findByUserAndRelatedUserId(user, userToBlockId);
        if (relation.isEmpty())
            throw new InvitationNotFoundException(userId, userToBlockId);
        if (relation.get().getRelationStatus() != RelationStatus.FRIENDS)
            throw new UserNotAllowedException(user.getId(), "block");
        relation.get().setRelationStatus(RelationStatus.BLOCKED);
        Relation relation2 = relationRepository.findByUserAndRelatedUserId(userRepository.findById(userToBlockId).orElseThrow(() -> new UserNotFoundException(userToBlockId)), user.getId()).get();
        relation2.setRelationStatus(RelationStatus.BLOCKED);
        relationRepository.save(relation2);
        return relationRepository.save(relation.get());
    }
}
