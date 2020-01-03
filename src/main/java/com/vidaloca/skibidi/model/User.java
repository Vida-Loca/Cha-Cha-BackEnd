package com.vidaloca.skibidi.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vidaloca.skibidi.registration.validation.ValidEmail;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @JsonManagedReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonBackReference
    @ManyToMany(mappedBy = "users", targetEntity = Event.class)
    List<Event> events;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<UserCard> userCards;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(unique = true)
   // @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @Column(columnDefinition = "boolean default false")
    private boolean enabled;
}
