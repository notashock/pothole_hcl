// UserService.java
package com.pothole_report.server.services;

import com.pothole_report.server.Utils.JwtService;
import com.pothole_report.server.dtos.LoginRequest;
import com.pothole_report.server.dtos.LoginResponse;
import com.pothole_report.server.dtos.RegisterRequest;
import com.pothole_report.server.enums.Role;
import com.pothole_report.server.models.User;
import com.pothole_report.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public List<User> getAllUsers(Role role) {
        if (role != null) {
            return repo.findAll().stream()
                    .filter(u -> u.getRole() == role)
                    .collect(Collectors.toList());
        }
        return repo.findAll();
    }

    public User registerUser(RegisterRequest user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setRole(user.getRole());
        return repo.save(newUser);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = repo.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials. Incorrect password.");
        }

        String token = jwtService.generateToken(user.getEmail());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getEmail());
        response.setRole(user.getRole());
        response.setMessage("Login Successful");
        return response;
    }
}