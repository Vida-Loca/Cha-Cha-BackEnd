package com.vidaloca.skibidi.friendship.model;

import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "invitation")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="invitation_id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="invitor_id")
    private User invitor;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="invited_id")
    private User invited;

    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus = InvitationStatus.PROCESSING;

}
