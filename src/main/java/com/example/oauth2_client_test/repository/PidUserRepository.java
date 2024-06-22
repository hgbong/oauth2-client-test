package com.example.oauth2_client_test.repository;

import com.example.oauth2_client_test.entity.PidUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PidUserRepository extends JpaRepository<PidUser, Long> {
    PidUser findByName(String name);
}
