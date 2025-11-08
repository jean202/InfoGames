package com.infogames.domain.user.entity;

import com.infogames.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 10)
    private String gender;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    public void updateProfile(String name, String email, String nickname) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
