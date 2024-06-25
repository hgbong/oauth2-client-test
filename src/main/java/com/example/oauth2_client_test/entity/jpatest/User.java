package com.example.oauth2_client_test.entity.jpatest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jpa_user")
@Getter
@NoArgsConstructor
public class User {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String userName;

    @OneToMany(mappedBy = "user")
    private Set<UserTeam> userTeams = new HashSet<>();

    public User(String userName) {
        this.userName = userName;
    }
}
