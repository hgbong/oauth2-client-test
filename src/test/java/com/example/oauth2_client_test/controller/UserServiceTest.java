package com.example.oauth2_client_test.controller;

import com.example.oauth2_client_test.entity.jpatest.Team;
import com.example.oauth2_client_test.entity.jpatest.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@SpringBootTest
@Transactional
class UserServiceTest {
    User user;
    Team team;

    @Autowired
    UserService userService;

    @BeforeEach
    public void beforeEach() {
        user = new User("사용자1");
        team = new Team("팀1");
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    void registerUser() {
        User savedUser = userService.registerUser(this.user);
        User user1 = userService.detailUser("사용자1");
        //Assert.isTrue(savedUser == user1, "same user");
    }

    @Test
    @Order(2)
    void listUsers() {

        /*
        * Class 어노테이션별 동작테스트 (@Rollback, @Tx)
        * 사전조건) 아래 다른 테스트 메소드 (registerUser)에서, 사용자가 insert 호출됨
        *
        * 1. 아무 어노테이션 사용 안했을 때
        *   결과: existUser 조회됨
        *   이유: registerUser에서 insert된 사용자가, 롤백되지 않음 (즉 Service의 @Tx을 따름)
        *
        * 2. @Rollback(false) 만 적용
        *   결과: 1과 동일
        *   이유: @Tx 설정하지 않을 경우 @Rollback적용되지 않는다고 함
        *
        * 3. @Tx 적용
        *   결과: null 리턴
        *   이유: SpringBootTest에서 @Tx -> TestExecutionListener 등록 -> @Test 전후로 Rollback Tx 설정 -> 따라서 insert 끝나고 롤백됨
        *
        * 4. @Tx + @Rollback(false)
        *   결과: 객체 리턴
        *   이유:
        *
        * */

        User existUser = userService.detailUser("사용자1");
        Assert.isNull(existUser, "Tx 컨디션에 따라 다를 수 있음");
    }

}