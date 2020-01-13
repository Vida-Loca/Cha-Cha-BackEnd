package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.EventUser;
import com.vidaloca.skibidi.model.UserCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCardRepository extends CrudRepository<UserCard,Integer> {
    List<UserCard> findAllByEventUser(EventUser eventUser);
}
