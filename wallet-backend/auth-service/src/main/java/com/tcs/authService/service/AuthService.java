package com.tcs.authService.service;

import com.tcs.authService.bean.User;
import com.tcs.authService.dto.*;
import com.tcs.authService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService; 

    public LoginResponse register(RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword())); 
        user.setStatus("ACTIVE"); 

        User savedUser = userRepository.save(user);
        return new LoginResponse(savedUser.getUserId(), savedUser.getUsername(), "Registration Successful");
    }

    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("Account is blocked. Please contact support.");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate Token
        String token = jwtService.generateToken(user.getUsername(), user.getUserId());

        // Return Token
        return new LoginResponse(user.getUserId(), user.getUsername(), "Login Successful", token);
    }
    
    public String getEmailByUserId(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return user.getEmail();
    }
}
