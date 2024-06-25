package com.example.oauth2_client_test.repository.jpa;

import com.example.oauth2_client_test.entity.jpatest.Product;
import com.example.oauth2_client_test.entity.jpatest.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
