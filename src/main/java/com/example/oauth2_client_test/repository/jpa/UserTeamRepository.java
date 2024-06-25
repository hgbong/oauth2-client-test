package com.example.oauth2_client_test.repository.jpa;

import com.example.oauth2_client_test.entity.jpatest.User;
import com.example.oauth2_client_test.entity.jpatest.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}
