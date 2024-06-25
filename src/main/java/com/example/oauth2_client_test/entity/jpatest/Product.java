package com.example.oauth2_client_test.entity.jpatest;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jpa_product")
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String productName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Product(String productName) {
        this.productName = productName;
    }

    public void setUser(User user) {
        this.user = user;
        user.getProducts().add(this);
    }
}
