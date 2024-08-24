package org.rares.userservice.service;

import org.rares.userservice.model.User;
import org.rares.userservice.model.UserDTO;
import org.rares.userservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Set default role
        userRepo.save(user);
    }

    public UserDTO getUserDetails(String email) {
        User user = userRepo.findByEmail(email).get();

        if (user != null) {
            return new UserDTO(user.getUsername(), user.getRole());
        }
        return null;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public void updateUserRole(String email, String newRole) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setRole(newRole);
        userRepo.save(user);
    }
}
