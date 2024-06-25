package com.example.oauth2_client_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 단순히 이 bean만 등록해주면, 바로 서비스(컨트롤러 등) 에서 사용 가능
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                // for test
                .requestMatchers("/**").permitAll()

                .requestMatchers("/", "/home", "/index", "/signup").permitAll() // "/form/login 은 아래에서 permit
                .requestMatchers("/users").permitAll() // 회원가입, 목록조회용
                .requestMatchers("/css/**", "/js/**", "/images/**","static/**", "/favicon.ico").permitAll() // 정적 리소스에 대한 접근 허용
                .anyRequest().authenticated())

            .formLogin(formLogin -> formLogin
                // 인증없을 시 기본 login page = "/login"
                // 그리고 (시큐리티에서 로그인 시도로 간주+처리하는) login processing url = "/login"
                //      (즉 <form action="/login"> 이면, 로그인으로 처리하겠다. 주의: input 태그 "username" 명 사용. 변경가능)
                .loginPage("/form/login")
                .loginProcessingUrl("/form/login")
                .defaultSuccessUrl("/home?loginSuccess", true)
                .failureUrl("/form/login?error")
                .permitAll()) // 위에 써줘도 되는데, 그럼 경로를 한번 더 써줘야되니 여기서 작성

            // .oauth2Login(Customizer.withDefaults()) // 커스터마이징 한다면 굳이 적용 X
            // form 로그인이 oauth 보다 우선순위 (oauth는 명시적 요청한 경우)
            //      formLogin.loginPage("/aa") , oauth2Login.loginPage("/bb") -> 인증필요한 url 요청 시 /aa로 이동
            // form 로그인이 없고, authz-server 2개 이상 등록한 경우, /login 에서 인가서버 선택 화면 제공
            // form 로그인이 없고, authz-server 1개인 경우, 바로 해당 인가서버로 리다이렉트 (/o/a/{r})

            .oauth2Login(oauth2Login -> oauth2Login
                .defaultSuccessUrl("/userInfo?oauth2LoginSuccess", true))

            .logout(logout -> logout
                    .logoutSuccessUrl("/logout?success")
                    .permitAll());

        return http.build();
    }
}
