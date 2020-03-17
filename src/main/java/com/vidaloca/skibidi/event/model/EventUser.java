package com.vidaloca.skibidi.event.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vidaloca.skibidi.product.model.UserCard;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "event_user")
@Data
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
    private int id;

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
    @OneToMany(mappedBy = "eventUser",cascade =CascadeType.ALL )
    private List<UserCard> userCard;

    @NotNull(message = "isAdmin cannot be null")
    private boolean isAdmin = false;

}
