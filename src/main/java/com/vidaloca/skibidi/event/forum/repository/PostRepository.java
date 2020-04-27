package com.vidaloca.skibidi.event.forum.repository;

import com.vidaloca.skibidi.event.forum.model.Post;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByEventUser_EventOrderByTimePostedDesc(Event event);
    List<Post> findAllByEventUserOrderByTimePostedDesc(EventUser eventUser);
}
