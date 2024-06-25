package com.example.oauth2_client_test.controller;

import com.example.oauth2_client_test.entity.jpatest.Team;
import com.example.oauth2_client_test.entity.jpatest.User;
import com.example.oauth2_client_test.entity.jpatest.UserTeam;
import com.example.oauth2_client_test.repository.jpa.UserRepository;
import com.example.oauth2_client_test.repository.jpa.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    public User registerUser(User user) {
        // 팀이 없는 유저 생성
        return userRepository.save(user);

    }

    public User addUserTeam(User user, Team team) {
        // 유저의 팀 목록에 team을 추가
        userTeamRepository.save(new UserTeam(user, team));
        return user;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User detailUser(String userName) {
        Optional<User> opUser = userRepository.findByUserName(userName);
        if (!opUser.isPresent()) {
            return null;
        }
        return opUser.get();
    }

}
