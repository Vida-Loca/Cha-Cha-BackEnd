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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "event_user_id")
    private EventUser eventUser;

    @NotNull(message = "Post cannot be empty")
    private String text;

    @Builder.Default
    private LocalDateTime timePosted = LocalDateTime.now();

    @Builder.Default
    private Integer likes = 0;

    @ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
     @JoinTable(name = "event_user_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "event_user_id")
    )
    private List<EventUser> likers = new ArrayList<>();

    @Builder.Default
    private boolean isUpdated = false;
}
