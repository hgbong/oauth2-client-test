package com.example.oauth2_client_test.controller;

import com.example.oauth2_client_test.config.PidUserDetails;
import com.example.oauth2_client_test.entity.PidUser;
import com.example.oauth2_client_test.repository.PidUserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TestController {
    @Autowired PidUserRepository pidUserRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @ResponseBody
    @GetMapping(value = {"/", "/home","/login", "/index"})
    public String home() { return "access allowed!!!"; }

    // 만약 ss6 설정에서 permit 안해줬으면 => 401
    @ResponseBody
    @PostMapping("/users")
    public void createUser(@RequestBody Map<String, Object> dto) {
        PidUser pidUser = new PidUser();
        pidUser.setName((String) dto.get("name"));

        // 고려사항: 로그인 쪽도 pE.matches 를 써야 한다. pE.matches(input_pw, db_encoded_pw)
        pidUser.setPassword(passwordEncoder.encode((String) dto.get("password")));
        pidUserRepository.save(pidUser);
    }

    @ResponseBody
    @GetMapping("/userInfo")
    public String userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails loggedInUser = (PidUserDetails) authentication.getPrincipal();// PidUserDetails type
        Assert.notNull(loggedInUser, "need authentication url.");

        return String.format("login success. username: %s, authorities: %s", loggedInUser.getUsername(), loggedInUser.getAuthorities());
    }

    @ResponseBody
    @GetMapping("/users")
    public List<UserResponseDto> listUsers() {
        return pidUserRepository.findAll()
                .stream()
                .map(e -> new UserResponseDto(e.getName(), e.getPassword()))
                .collect(Collectors.toList());
    }

    // View

    @GetMapping("/form/login")
    public String formLogin() {
        return "th/form/login";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class UserResponseDto {
        private String name;
        private String password;
    }
}
