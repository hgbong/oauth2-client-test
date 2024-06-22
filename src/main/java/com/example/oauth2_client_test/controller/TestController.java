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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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

        // 고려사항: 로그인 쪽도 pE.matches 쓰게 되지만, 개발자는 신경 X
        //   (UserDetailsService 구현 시 DB 조회한 유저의 pw를 UserDetails.password 그대로 전달 (어차피 DB상 암호화된 상태)
        pidUser.setPassword(passwordEncoder.encode((String) dto.get("password")));
        pidUserRepository.save(pidUser);
    }

    // 여러 인증방식을 사용할 경우, 인증 객체 타입이 다른 문제가 있음
    // 따라서 서비스에서 사용할 인증 인터페이스를 통합하거나 커스터마이징할 필요 있음
    //      인터페이스 타입:           Authentication                           Principal (UserDetails)
    //      username/password 인증:   UsernamePAsswordAuthenticationToken      PidUserDetails(커스텀 유형)
    //      oauth2 인증:              OAuth2AuthenticationToken                OAuth2User
    //      oidc 스코프:              OAuth2AuthenticationToken(?)             OidcUser(extends OAuth2User)
    //      saml 인증:                ...


    // username password 인증 방식
    @ResponseBody
    @GetMapping("/userInfo-usernamepassword")
    public String userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails loggedInUser = (PidUserDetails) authentication.getPrincipal();// PidUserDetails
        Assert.notNull(loggedInUser, "need authentication url.");

        return String.format("login success. username: %s, authorities: %s", loggedInUser.getUsername(), loggedInUser.getAuthorities());
    }
    // oauth2 인증 방식 (SecurityContextHolder 사용해도 무관)
    @ResponseBody
    @GetMapping("/userInfo-oauth2")
    public String userInfo2(@AuthenticationPrincipal OAuth2User oAuth2User) { // DefaultOidcUser
        System.out.println(oAuth2User.getAuthorities().toString());

        return String.format("user success.");
    }

    // usernamepassword, oauth2 인증객체 통합해본 방법..?
    @ResponseBody
    @GetMapping("/userInfo")
    public String userInfo3() {
        setIntegUser();
        return "";
    }

    // TODO 필터에 반영 + 팩토리
    private void setIntegUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("user not authenticated.");
        }

        if (authentication.getPrincipal() instanceof UserDetails) {
            PidUserDetails principal = (PidUserDetails) authentication.getPrincipal();

            SecurityContextHolder.getContext().setAuthentication(convertFromUserDetails(principal));
        } else if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(convertFromUserOidcUser(oidcUser));
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OidcUser) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(convertFromUserOauth2User(oAuth2User));
        } else {
            throw new RuntimeException("not happened.");
        }
    }

    private Authentication convertFromUserOauth2User(OAuth2User oAuth2User) {
        throw new UnsupportedOperationException("not implemented");
    }

    private Authentication convertFromUserOidcUser(OidcUser oidcUser) {
        throw new UnsupportedOperationException("not implemented");
    }

    private Authentication convertFromUserDetails(PidUserDetails principal) {
        throw new UnsupportedOperationException("not implemented");
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
