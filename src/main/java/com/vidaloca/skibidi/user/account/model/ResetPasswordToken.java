package com.vidaloca.skibidi.user.account.model;

import com.vidaloca.skibidi.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="reset_password_token")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public ResetPasswordToken(User user, String token){
        this.token = token;
        this.user = user;
        expiryDate = LocalDateTime.now().plusDays(1);
    }
}