package com.example.oauth2_client_test.config;

import com.example.oauth2_client_test.entity.PidUser;
import com.example.oauth2_client_test.repository.PidUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PidUserDetailsService implements UserDetailsService {

    @Autowired PidUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(String.format("@@@@@@loadUserByUsername. username parameter = %s", username));

        PidUser user = userRepository.findByName(username);
        if (user == null) return null;

        PidUserDetails pidUserDetails = new PidUserDetails();
        pidUserDetails.setName(user.getName());
        pidUserDetails.setPassword(user.getPassword());

        return pidUserDetails;
    }
}
