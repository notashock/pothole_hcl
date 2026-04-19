// UserController.java
package com.pothole_report.server.controller;

import com.pothole_report.server.services.UserService;
import com.pothole_report.server.dtos.LoginRequest;
import com.pothole_report.server.dtos.LoginResponse;
import com.pothole_report.server.dtos.RegisterRequest;
import com.pothole_report.server.enums.Role;
import com.pothole_report.server.models.User;
import com.pothole_report.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody RegisterRequest user){
        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        service.registerUser(user);
        return ResponseEntity.ok("Registration Successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest user) {
        try {
            LoginResponse response = service.loginUser(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) Role role) {
        List<User> users = service.getAllUsers(role);
        return ResponseEntity.ok(users);
    }
}