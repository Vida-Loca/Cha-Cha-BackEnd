package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.exception.model.UserIsNotAdminException;
import com.vidaloca.skibidi.user.model.User;

import java.util.List;

public interface EventUserService {

    List<User> findAllEventUsers(Long eventId, Long currentUserId);

    String deleteUser(Long eventId, Long userToDeleteId, Long userId) throws UserIsNotAdminException;

    String grantUserAdmin(Long eventId, Long userToGrantId, Long user_id) throws UserIsNotAdminException;

    boolean isCurrentUserAdminOfEvent(Long eventId, Long currentUserId);

    boolean leaveEvent(Long eventId, Long currentUserId);

    List<User> findAllEventAdmins(Long eventId, Long currentUserId);
}
