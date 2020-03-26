package com.vidaloca.skibidi.friendship.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vidaloca.skibidi.friendship.status.InvitationStatus;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="friendship_id")
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

    @JsonFormat(pattern = "yyy-MM-dd HH:mm")
    private LocalDateTime friendshipStartTime;


    public static final class FriendshipBuilder {
        private Long id;
        private User invitor;
        private User invited;
        private InvitationStatus invitationStatus;
        private LocalDateTime friendshipStartTime;

        private FriendshipBuilder() {
        }

        public static FriendshipBuilder aFriendship() {
            return new FriendshipBuilder();
        }

        public FriendshipBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public FriendshipBuilder withInvitor(User invitor) {
            this.invitor = invitor;
            return this;
        }

        public FriendshipBuilder withInvited(User invited) {
            this.invited = invited;
            return this;
        }

        public FriendshipBuilder withInvitationStatus(InvitationStatus invitationStatus) {
            this.invitationStatus = invitationStatus;
            return this;
        }

        public FriendshipBuilder withFriendshipStartTime(LocalDateTime friendshipStartTime) {
            this.friendshipStartTime = friendshipStartTime;
            return this;
        }

        public Friendship build() {
            Friendship friendship = new Friendship();
            friendship.setId(id);
            friendship.setInvitor(invitor);
            friendship.setInvited(invited);
            friendship.setInvitationStatus(invitationStatus);
            friendship.setFriendshipStartTime(friendshipStartTime);
            return friendship;
        }
    }
}
