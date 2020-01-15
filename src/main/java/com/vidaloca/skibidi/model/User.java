package com.vidaloca.skibidi.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "event")
    private List<EventUser> event_user;


    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
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

    @JsonIgnore
    @Column(columnDefinition = "boolean default false")
    private boolean enabled;

}
