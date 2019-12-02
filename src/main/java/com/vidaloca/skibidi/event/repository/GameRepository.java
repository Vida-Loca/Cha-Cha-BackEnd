package com.vidaloca.skibidi.event.repository;

import com.vidaloca.skibidi.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game,Integer> {
}
