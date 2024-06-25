package com.example.oauth2_client_test.entity.jpatest;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jpa_user_team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeam {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id") // rcn=> 레퍼런스하는 컬럼 (즉, USER 테이블의 PK)
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_123")
    private Team team;

    public UserTeam(User user, Team team) {
        this.user = user;
        this.team = team;

        user.getUserTeams().add(this);
        team.getUserTeams().add(this);
    }
}
