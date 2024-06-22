package com.example.oauth2_client_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
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
                .requestMatchers("/", "/home", "/index", "/form/login", "/h2-console").permitAll()
                .requestMatchers("/users").permitAll()

                .requestMatchers("/css/**", "/js/**", "/images/**","static/**", "/favicon.ico").permitAll() // 정적 리소스에 대한 접근 허용
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                // 인증없을 시 기본 login page = "/login"
                // 그리고 (시큐리티에서 로그인 시도로 간주+처리하는) login processing url = "/login"
                //      (즉 <form action="/login"> 이면, 로그인으로 처리하겠다. 주의: input 태그 "username" 명 사용)

                .loginPage("/form/login")
                .loginProcessingUrl("/form/login")
                .defaultSuccessUrl("/home?loginSuccess", true)
                .failureUrl("/form/login?error")
                .permitAll() // 위에 써줘도 되는데, 그럼 경로를 한번 더 써줘야되니 여따쓰자.
            );

        return http.build();
    }


//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .oauth2Login(Customizer.withDefaults());
//        return http.build();
//    }
}
