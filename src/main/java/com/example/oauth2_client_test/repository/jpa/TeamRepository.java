package com.example.oauth2_client_test.repository.jpa;

import com.example.oauth2_client_test.entity.jpatest.Team;
import com.example.oauth2_client_test.entity.jpatest.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
