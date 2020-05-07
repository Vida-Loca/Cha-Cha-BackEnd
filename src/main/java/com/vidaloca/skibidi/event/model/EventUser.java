package com.vidaloca.skibidi.event.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vidaloca.skibidi.event.forum.model.Post;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUser {
    public EventUser(User user, Event event, boolean isAdmin){
        this.user = user;
        this.event =event;
        this.isAdmin = isAdmin;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_user_id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "event_id")
    private Event event;

    @JsonIgnore
    @OneToMany (mappedBy = "eventUser",  cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Builder.Default
    private List<Product> products = new ArrayList<>();
    @JsonIgnore
    @Builder.Default
    @ManyToMany (cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "event_user_post",
            joinColumns = @JoinColumn(name = "event_user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> likes =  new ArrayList<>();

    @NotNull(message = "isAdmin cannot be null")
    @Builder.Default
    private boolean isAdmin = false;
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "eventUser")
    private List<Post> posts = new ArrayList<>();

}
