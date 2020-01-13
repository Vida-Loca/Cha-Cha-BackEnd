package com.vidaloca.skibidi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    private Address address;

    @JsonManagedReference
    @ManyToMany(mappedBy = "events", targetEntity = Game.class)
    private Set<Game> games;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "event_product",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    @JsonBackReference
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL)
    private List<EventUser> eventUsers;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name="start_date")
    private Date startDate;

}
