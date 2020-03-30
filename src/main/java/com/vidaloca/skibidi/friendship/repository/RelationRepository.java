package com.vidaloca.skibidi.friendship.repository;

import com.vidaloca.skibidi.friendship.model.Relation;
import com.vidaloca.skibidi.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends CrudRepository<Relation,Long> {
    List<Relation> findAllByUser(User user);
    Optional<Relation> findByUserAndRelatedUserId(User user, Long relatedUserId);
}
