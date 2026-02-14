package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.User;
import com.medical.webconsultation.model.Role;
import com.medical.webconsultation.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")  // Adjust as needed
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("📨 Registration attempt for: " + user.getEmail());

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email is required."));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email is already registered."));
        }

        if (user.getRole() == null) {
            user.setRole(Role.patient); // Default to patient
        }

        try {
            User savedUser = userRepository.save(user);
            savedUser.setPassword(null); // Don't return password
            return ResponseEntity.ok(savedUser);  // ✅ Return User object (not string)
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Registration failed: " + e.getMessage()));
        }
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Email and password are required."));
        }

        User found = userRepository.findByEmail(user.getEmail());

        if (found == null || !found.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid credentials"));
        }

        found.setPassword(null); // Hide password
        return ResponseEntity.ok(found);
    }
}
