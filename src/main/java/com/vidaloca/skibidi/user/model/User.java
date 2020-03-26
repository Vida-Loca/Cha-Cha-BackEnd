package com.vidaloca.skibidi.user.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.Nullable;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.friendship.model.Friendship;
import com.vidaloca.skibidi.user.registration.validation.ValidEmail;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "userr")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    private List<EventUser> eventUsers;


    @Column(unique = true)
    @NotNull(message = "Username is obligatory")
    private String username;

    @JsonIgnore
    @NotNull(message = "Password cannot be null")
    @Length(min=60,max=60,message = "Password crashed")
    private String password;

    private String name;

    private String surname;

    private String picUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime joined;

    @Column(unique = true)
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @OneToMany(mappedBy = "invitor", cascade = CascadeType.ALL)
    private List<Friendship> invitorFriendships;

    @OneToMany(mappedBy = "invited", cascade = CascadeType.ALL)
    private List<Friendship> invitedFriendships;

    @JsonIgnore
    private boolean enabled = false;

    @JsonIgnore
    private boolean canChangePass = false;

    @Override
    public String toString() {
        return username;
    }
}
