package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.UserCard;
import org.springframework.data.repository.CrudRepository;

public interface UserCardRepository extends CrudRepository<UserCard,Integer> {
}
