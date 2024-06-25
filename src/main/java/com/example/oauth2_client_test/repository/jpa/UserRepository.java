package com.example.oauth2_client_test.repository.jpa;

import com.example.oauth2_client_test.entity.jpatest.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
