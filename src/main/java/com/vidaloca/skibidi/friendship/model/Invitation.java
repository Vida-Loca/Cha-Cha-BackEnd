package com.vidaloca.skibidi.friendship.model;

import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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

    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus;


    public static final class InvitationBuilder {
        private Long id;
        private User invitor;
        private User invited;
        private InvitationStatus invitationStatus;

        private InvitationBuilder() {
        }

        public static InvitationBuilder anInvitation() {
            return new InvitationBuilder();
        }

        public InvitationBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public InvitationBuilder withInvitor(User invitor) {
            this.invitor = invitor;
            return this;
        }

        public InvitationBuilder withInvited(User invited) {
            this.invited = invited;
            return this;
        }

        public InvitationBuilder withInvitationStatus(InvitationStatus invitationStatus) {
            this.invitationStatus = invitationStatus;
            return this;
        }

        public Invitation build() {
            Invitation invitation = new Invitation();
            invitation.setId(id);
            invitation.setInvitor(invitor);
            invitation.setInvited(invited);
            invitation.setInvitationStatus(invitationStatus);
            return invitation;
        }
    }
}
