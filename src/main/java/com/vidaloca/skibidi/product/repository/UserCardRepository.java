package com.vidaloca.skibidi.product.repository;

import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.model.UserCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCardRepository extends CrudRepository<UserCard,Integer> {
    List<UserCard> findAllByEventUser(EventUser eventUser);
}
