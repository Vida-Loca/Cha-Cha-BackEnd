package com.vidaloca.skibidi.user.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vidaloca.skibidi.user.model.User;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(unique = true, nullable = false)
    private String name;
    @JsonBackReference
    @OneToMany(
            mappedBy = "role",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    private List<User> users;

    @Override
    public String toString() {
        return name ;
    }
}