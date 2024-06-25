package com.example.oauth2_client_test.entity.jpatest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jpa_user")
@Getter
@NoArgsConstructor
public class User {
    // User:Team = N:M
    // User:Product = 1:N

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String userName;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<UserTeam> userTeams = new ArrayList<>();

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Product> products = new ArrayList<>();

    public User(String userName) {
        this.userName = userName;
    }
}
