package com.vidaloca.skibidi.friendship.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vidaloca.skibidi.friendship.status.RelationStatus;
import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_friend")
public class Relation {

    @Id
    @Column(name = "friend_id")
    private Long relatedUserId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    private LocalDateTime relationStartDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @NotNull
    private RelationStatus relationStatus;


    public static final class RelationBuilder {
        private Long relatedUserId;
        private User user;
        private LocalDateTime relationStartDate = LocalDateTime.now();
        private RelationStatus relationStatus;

        private RelationBuilder() {
        }

        public static RelationBuilder aRelation() {
            return new RelationBuilder();
        }

        public RelationBuilder withRelatedUserId(Long relatedUserId) {
            this.relatedUserId = relatedUserId;
            return this;
        }

        public RelationBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public RelationBuilder withRelationStartDate(LocalDateTime relationStartDate) {
            this.relationStartDate = relationStartDate;
            return this;
        }

        public RelationBuilder withRelationStatus(RelationStatus relationStatus) {
            this.relationStatus = relationStatus;
            return this;
        }

        public Relation build() {
            Relation relation = new Relation();
            relation.setRelatedUserId(relatedUserId);
            relation.setUser(user);
            relation.setRelationStartDate(relationStartDate);
            relation.setRelationStatus(relationStatus);
            return relation;
        }
    }
}
