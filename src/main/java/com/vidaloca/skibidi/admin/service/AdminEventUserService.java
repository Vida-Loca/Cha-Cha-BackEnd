package com.vidaloca.skibidi.admin.service;

import com.vidaloca.skibidi.user.model.User;

import java.util.List;

public interface AdminEventUserService {

    List<User> findAllEventUsers(Long eventId);

    String deleteUserFromEvent(Long userToDeleteId, Long eventId);

    boolean grantTakeUserEventAdmin(Long eventId, Long userId);

    List<User> findAllEventAdmins(Long eventId);

}
