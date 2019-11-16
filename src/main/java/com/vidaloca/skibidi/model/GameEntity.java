package com.vidaloca.skibidi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "games")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private int id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(
            name = "event_game",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    Set<EventEntity> events;
}
