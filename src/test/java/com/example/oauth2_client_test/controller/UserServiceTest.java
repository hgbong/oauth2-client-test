package com.example.oauth2_client_test.controller;

import com.example.oauth2_client_test.entity.jpatest.Product;
import com.example.oauth2_client_test.entity.jpatest.Team;
import com.example.oauth2_client_test.entity.jpatest.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
@Transactional // @Tx 사용 시 주의사항: 만약 service 클래스쪽에 @Tx가 없다면, 그래도 테스트 성공할 수는 있음. 그러나 프로덕션 환경에서 예외 발생함 (이유:____)
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
    // @Rollback(value = false)
    void registerUser() {
        User savedUser = userService.registerUser(this.user);
        User user1 = userService.detailUser("사용자1");

        /*
        * Class Tx 어노테이션별 동작테스트
        *
        * 1. 테스트 클래스or메서드에 @Tx 없는 경우
        *   결과: 테스트 실패
        *   이유: 테스트클래스쪽에 tx 없으므로 최초 Service tx를 사용. 그럼 37L, 38L에서 각각 service를 호출하기 때문에 독립된 tx 생성됨
        *
        * 2. 테스트 클래스에 @Tx 있는 경우
        *   결과: 테스트 성공
        *   이유: 테스트클래스쪽 tx로 인해, (rollback) tx가 상단에 묶임. Service tx의 propagation 기본 설정이므로, 상단 tx를 따름. 즉 37,38L의 tx은 같은 영역임
        * */
        Assert.isTrue(savedUser == user1, "same user");
    }

    @Test
    @Order(2)
    void listUsersForRollbackTest() {

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

    @PersistenceContext
    EntityManager em;

    @Test
    public void listUserQueryTest() {
        // 유저 100명이 추가됐을 때, 페이징 처리
        for (int i = 0; i < 20; i++) {
            User user = new User("name" + i);
            Team team = new Team("team" + i);
            Product product = new Product("product" + i);

            /*
            * 복기용 에러 코드
            * // Caused by: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : com.example.oauth2_client_test.entity.jpatest.UserTeam.team -> com.example.oauth2_client_test.entity.jpatest.Team
                    userService.registerUser(user);
                    userService.addUserTeam(user, team);
                    userService.addUserProduct(user, product); // CONSIDER userService의 역할은 아님
            * */
            // 예상원인: User만 save하고, 나머진 영속성 전이 안돼서 그런듯 -> Cascade persist만 주고 다시 테스트!


            userService.registerUser(user);
            userService.addUserTeam(user, team);
            userService.addUserProduct(user, product);
        }

        em.flush(); // 굳이 호출 X. 쿼리 확인용
        em.clear();

        // 20명의 유저. 10개씩 이므로 쿼리는 총 5번 생기길 예상
        // 이유: 유저 목록 조회(1번) + 10개씩 UserTeam 조회(2번) + 10개씩 Product 조회 (2번)

        List<User> users = userService.listAllUsers();
//        for (int i = 0; i < 20; i++) {
//        }

        users.get(15).getProducts().get(0).getProductName(); // proxy 구조때문에, users.get(i).getProducts() 까지만 가져오면 실제 쿼리 수행X
        users.get(15).getUserTeams().get(0).getTeam().getTeamName(); // 여기서는 15번째 하나만 가져오지만, batch size로 인해 user_id in (,,,,,,,) 실행됨
        Assert.isTrue(users.size()==20, "user count is 20.");


    }


}