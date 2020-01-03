package com.vidaloca.skibidi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int event_id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany(mappedBy = "events", targetEntity = Game.class)
    private Set<Game> games;

    @ManyToMany(mappedBy = "events", targetEntity = Product.class)
    private List<Product> products;

    @ManyToMany(mappedBy = "events", targetEntity = User.class)
    private Set<User> users;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<UserCard> userCards;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name="start_date")
    private Date startDate;

}
