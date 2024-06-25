package com.example.oauth2_client_test.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pid_user")
@Getter @Setter
public class PidUser {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;

    private LocalDateTime createdAt;
}
