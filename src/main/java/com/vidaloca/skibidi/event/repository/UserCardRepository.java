package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.Event_User;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.UserCard;
import org.springframework.data.repository.CrudRepository;

public interface UserCardRepository extends CrudRepository<UserCard,Integer> {
}
