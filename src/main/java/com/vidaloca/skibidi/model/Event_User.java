package com.vidaloca.skibidi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "event_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event_User {
    public Event_User(User user, Event event, boolean isAdmin){
        this.user = user;
        this.event =event;
        this.isAdmin = isAdmin;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_user_id")
    private int id;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "event_user",cascade ={CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH} )
    private List<UserCard> userCard;

    @Column(name ="is_admin", nullable = false, columnDefinition = "boolean default false")
    private boolean isAdmin;

}
