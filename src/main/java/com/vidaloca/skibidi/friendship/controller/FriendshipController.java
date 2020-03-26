package com.vidaloca.skibidi.friendship.controller;

import com.vidaloca.skibidi.friendship.model.Friendship;
import com.vidaloca.skibidi.friendship.service.FriendshipService;
import com.vidaloca.skibidi.user.account.current.CurrentUser;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class FriendshipController {
    private FriendshipService friendshipService;

    @Autowired
    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/users_contains")
    public List<User> getAllUsersContains(@RequestParam String regex){
        return friendshipService.findAllByUsernameContains(regex);
    }

    @GetMapping("/user/friends")
    public List<User> getAllCurrentUserFriends(HttpServletRequest request){
        return friendshipService.findAllUserFriends(CurrentUser.currentUserId(request));
    }
    @GetMapping("/user/{userId}/friends")
    public List<User> getAllUserFriends(@PathVariable Long userId){
        return friendshipService.findAllUserFriends(userId);
    }
    @GetMapping("/user/invitations")
    public List<Friendship> getAllInvitations(HttpServletRequest request){
        return friendshipService.findAllUserInvitations(CurrentUser.currentUserId(request));
    }
    @PostMapping("/user/invite")
    public Friendship inviteUserToFriends(HttpServletRequest request,@RequestParam Long invitedId){
        return friendshipService.inviteFriend(CurrentUser.currentUserId(request),invitedId);
    }
    @PutMapping("/user/cancel")
    public Friendship cancelInvitation(HttpServletRequest request, @RequestParam Long friendshipId){
        return friendshipService.cancelInvitation(friendshipId,CurrentUser.currentUserId(request));
    }
    @PutMapping("/user/accept")
    public Friendship acceptInvitation(HttpServletRequest request, @RequestParam Long friendshipId){
        return friendshipService.acceptInvitation(friendshipId,CurrentUser.currentUserId(request));
    }
    @PutMapping("/user/reject")
    public Friendship rejectInvitation(HttpServletRequest request, @RequestParam Long friendshipId){
        return friendshipService.rejectInvitation(friendshipId,CurrentUser.currentUserId(request));
    }
    @PutMapping("/user/remove")
    public Friendship removeFriend(HttpServletRequest request, @RequestParam Long userToRemoveId){
        return friendshipService.removeFriend(CurrentUser.currentUserId(request),userToRemoveId);
    }
}
