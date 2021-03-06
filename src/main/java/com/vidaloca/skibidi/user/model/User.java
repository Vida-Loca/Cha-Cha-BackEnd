package com.vidaloca.skibidi.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vidaloca.skibidi.event.access.model.EventInvitation;
import com.vidaloca.skibidi.event.access.model.EventRequest;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.friendship.model.Invitation;
import com.vidaloca.skibidi.friendship.model.Relation;
import com.vidaloca.skibidi.user.registration.validation.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "userr")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private List<EventUser> eventUsers = new ArrayList<>();


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
    @Builder.Default
    private LocalDateTime joined = LocalDateTime.now();

    @Column(unique = true)
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "invitor", cascade = CascadeType.ALL)
    private List<Invitation> invitationsFromUser = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "invited", cascade = CascadeType.ALL)
    private List<Invitation> invitationsToUser = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Relation> relations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EventInvitation> eventInvitations = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EventRequest> eventRequests = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    private boolean enabled = false;

    @Builder.Default
    private boolean isBanned = false;

    @Override
    public String toString() {
        return username;
    }
}
