package com.vidaloca.skibidi.event.forum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vidaloca.skibidi.event.model.EventUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "event_user_id")
    private EventUser eventUser;

    @NotNull(message = "Post cannot be empty")
    private String text;

    @Builder.Default
    private LocalDateTime timePosted = LocalDateTime.now();

    @Builder.Default
    private Integer likes = 0;

    @ManyToMany( mappedBy = "likes",  cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private Set<EventUser> likers = new HashSet<>();

    @Builder.Default
    private boolean isUpdated = false;
}
