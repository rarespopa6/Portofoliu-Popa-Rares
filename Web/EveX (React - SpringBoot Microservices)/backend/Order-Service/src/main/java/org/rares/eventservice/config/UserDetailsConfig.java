package org.rares.eventservice.config;

import org.rares.eventservice.feign.UserServiceClient;
import org.rares.eventservice.model.UserDTO;
import org.rares.eventservice.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsConfig implements UserDetailsService {

    @Autowired
    private UserServiceClient userServiceClient;


    public UserDetails loadUserByUsername(String username, String token) throws UsernameNotFoundException {
        UserDTO userDTO = userServiceClient.getUserByUsername("Bearer " + token, username);
        if (userDTO == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new UserPrincipal(userDTO.getEmail(), userDTO.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Use loadUserByUsername with token instead.");
    }
}
