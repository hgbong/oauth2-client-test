package com.example.oauth2_client_test.entity.jpatest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jpa_team")
@Getter
@NoArgsConstructor
public class Team {
    @Id @GeneratedValue
    private Long id;

    private String teamName;

    @OneToMany(mappedBy = "team")
    private Set<UserTeam> userTeams = new HashSet<>();

    public Team(String teamName) {
        this.teamName = teamName;
    }
}
