package com.vidaloca.skibidi.friendship.service;

import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.friendship.model.Invitation;
import com.vidaloca.skibidi.friendship.model.Relation;
import com.vidaloca.skibidi.friendship.repository.InvitationRepository;
import com.vidaloca.skibidi.friendship.repository.RelationRepository;
import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.friendship.status.RelationStatus;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceImplTest {

    @Mock(lenient = true)
    UserRepository userRepository;
    @Mock
    InvitationRepository invitationRepository;
    @Mock
    RelationRepository relationRepository;

    @InjectMocks
    FriendshipServiceImpl service;

    User invitor;
    User invited;
    Event event;
    EventUser eventUser;
    List<User> userList;

    @BeforeEach
    void setUp() {
        invitor = new User();
        invitor.setId(1L);
        invitor.setUsername("INVITOR");
        invited = new User();
        invited.setId(2L);
        invited.setUsername("INVITED");
        event = new Event();
        event.setId(1L);
        event.setName("EVENT");
        eventUser = new EventUser();
        eventUser.setId(1L);
        userList = new ArrayList<>();

        given(userRepository.findById(1L)).willReturn(Optional.of(invitor));
        given(userRepository.findById(2L)).willReturn(Optional.of(invited));
    }

    @Test
    void findAllByUsernameContains() {
        //given
        userList.add(invitor);
        given(userRepository.findAllByUsernameContains(anyString())).willReturn(userList);

        //when
        List<User> result = service.findAllByUsernameContains("INVITOR");

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("INVITOR", result.get(0).getUsername());
        then(userRepository).should().findAllByUsernameContains(anyString());
    }

    @Test
    void findAllUserFriends() {
        //given
        Relation invitor_relation = new Relation();
        invitor_relation.setRelationStatus(RelationStatus.FRIENDS);
        invitor_relation.setUser(invitor);
        invitor_relation.setRelatedUserId(2L);
        invitor.getRelations().add(invitor_relation);

        //when
        List<User> result = service.findAllUserFriends(1L);

        //then
        assertEquals("INVITED", result.get(0).getUsername());
        assertEquals(1, result.size());
        then(userRepository).should(times(2)).findById(anyLong());
    }

    @Test
    void findAllFriendsEvents() {
        //given
        Relation invitor_relation = new Relation();
        invitor_relation.setRelationStatus(RelationStatus.FRIENDS);
        invitor_relation.setUser(invitor);
        invitor_relation.setRelatedUserId(2L);
        invitor.getRelations().add(invitor_relation);

        eventUser.setUser(invited);
        eventUser.setEvent(event);
        List<EventUser> eventUsers = new ArrayList<>();
        eventUsers.add(eventUser);
        invited.setEventUsers(eventUsers);

        //when
        Set<Event> result = service.findAllFriendsEvents(1L);

        //then
        assertEquals(1, result.size());
        then(userRepository).should(times(2)).findById(anyLong());
    }

    @Test
    void findAllUserInvitations() {
        //given
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInvitationStatus(InvitationStatus.PROCESSING);
        invitation.setInvitor(invitor);
        invitation.setInvited(invited);
        invited.getInvitationsToUser().add(invitation);

        //when
        List<Invitation> result = service.findAllUserInvitations(2L);

        //then
        assertEquals(1, result.size());
        assertEquals("INVITOR", result.get(0).getInvitor().getUsername());
        then(userRepository).should().findById(anyLong());
    }

    @Test
    void inviteFriend() {
        //given
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInvitor(invitor);
        invitation.setInvited(invited);

        given(relationRepository.findByUserAndRelatedUserId(invitor, 2L)).willReturn(Optional.empty());
        given(invitationRepository.findByInvitorAndInvited(invitor, invited)).willReturn(Optional.empty());
        given(invitationRepository.save(any(Invitation.class))).willReturn(invitation);

        //when
        Invitation result = service.inviteFriend(1L, 2L);

        //then
        assertEquals("INVITOR", result.getInvitor().getUsername());
        assertEquals("INVITED", result.getInvited().getUsername());
        then(userRepository).should(times(2)).findById(anyLong());
        then(relationRepository).should().findByUserAndRelatedUserId(any(User.class), anyLong());
        then(invitationRepository).should().findByInvitorAndInvited(any(User.class), any(User.class));
        then(invitationRepository).should().save(any(Invitation.class));
    }

    @Test
    void cancelInvitation() {
        //given
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInvitor(invitor);
        invitation.setInvited(invited);

        given(invitationRepository.findById(1L)).willReturn(Optional.of(invitation));
        given(invitationRepository.save(any(Invitation.class))).willReturn(invitation);

        //when
        Invitation result = service.cancelInvitation(1L, 1L);

        //then
        assertEquals("Canceled", result.getInvitationStatus().getDescription());
        then(invitationRepository).should().findById(anyLong());
        then(invitationRepository).should().save(any(Invitation.class));
    }

    @Test
    void acceptInvitation() {
        //given
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInvitor(invitor);
        invitation.setInvited(invited);

        Relation relation2 = Relation.builder().user(invited).relatedUserId(invitor.getId()).
                relationStartDate(LocalDateTime.now()).relationStatus(RelationStatus.FRIENDS).build();

        given(invitationRepository.findById(1L)).willReturn(Optional.of(invitation));
        given(relationRepository.save(any(Relation.class))).willReturn(relation2);

        //when
        Relation result = service.acceptInvitation(1L, 2L);

        //then
        assertEquals("Friends", result.getRelationStatus().getDescription());
        then(userRepository).should().findById(anyLong());
        then(invitationRepository).should().findById(anyLong());
        then(invitationRepository).should().save(any(Invitation.class));
        then(relationRepository).should(times(2)).save(any(Relation.class));
    }

    @Test
    void rejectInvitation() {
        //given
        Invitation invitation = new Invitation();
        invitation.setId(1L);
        invitation.setInvitor(invitor);
        invitation.setInvited(invited);

        given(invitationRepository.findById(1L)).willReturn(Optional.of(invitation));
        given(invitationRepository.save(any(Invitation.class))).willReturn(invitation);

        //when
        Invitation result = service.rejectInvitation(1L, 2L);

        //then
        assertEquals("Rejected", result.getInvitationStatus().getDescription());
        then(invitationRepository).should().findById(anyLong());
        then(invitationRepository).should().save(any(Invitation.class));
    }

    @Test
    void removeFriend() {
        //given
        Relation relation = new Relation();
        relation.setRelationStatus(RelationStatus.FRIENDS);
        relation.setUser(invitor);
        relation.setRelatedUserId(2L);

        Invitation invitation = new Invitation();

        given(relationRepository.findByUserAndRelatedUserId(any(User.class), anyLong())).willReturn(Optional.of(relation));
        given(invitationRepository.findByInvitorAndInvited(invitor, invited)).willReturn(Optional.of(invitation));
        given(relationRepository.save(any(Relation.class))).willReturn(relation);

        //when
        Relation result = service.removeFriend(1L, 2L);

        //then
        assertEquals("Removed", result.getRelationStatus().getDescription());
        then(userRepository).should(times(2)).findById(anyLong());
        then(relationRepository).should(times(2)).findByUserAndRelatedUserId(any(User.class), anyLong());
        then(invitationRepository).should(atLeast(1)).findByInvitorAndInvited(any(User.class), any(User.class));
        then(invitationRepository).should().delete(any(Invitation.class));
        then(relationRepository).should(times(2)).save(any(Relation.class));
    }

    @Test
    void blockUser() {
        //given
        Relation relation = new Relation();
        relation.setRelationStatus(RelationStatus.FRIENDS);
        relation.setUser(invitor);
        relation.setRelatedUserId(2L);

        given(relationRepository.findByUserAndRelatedUserId(any(User.class), anyLong())).willReturn(Optional.of(relation));
        given(relationRepository.save(any(Relation.class))).willReturn(relation);

        //when
        Relation result = service.blockUser(1L, 2L);

        //then
        assertEquals("Blocked", result.getRelationStatus().getDescription());
        then(userRepository).should(times(2)).findById(anyLong());
        then(relationRepository).should(times(2)).findByUserAndRelatedUserId(any(User.class), anyLong());
        then(relationRepository).should(times(2)).save(any(Relation.class));
    }
}