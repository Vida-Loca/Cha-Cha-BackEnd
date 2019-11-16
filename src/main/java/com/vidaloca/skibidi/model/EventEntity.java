package com.vidaloca.skibidi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int event_id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ManyToMany(mappedBy = "events", targetEntity = GameEntity.class)
    private Set<GameEntity> games;

    @ManyToMany(mappedBy = "events", targetEntity = ProductEntity.class)
    private Set<ProductEntity> products;

    @ManyToMany(mappedBy = "events", targetEntity = UserEntity.class)
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<UserCardEntity> userCards;

    @Column(nullable = false)
    private String name;

}
