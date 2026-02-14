package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.User;
import com.medical.webconsultation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id).orElse(null);
    }

    // Get user by Email
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return user;
    }

    // Upload Profile Image (Base64 string)
    @PostMapping("/upload-image/{id}")
    public String uploadProfileImage(@PathVariable int id, @RequestBody User userRequest) {
        return userRepository.findById(id).map(user -> {
            user.setProfileImage(userRequest.getProfileImage());
            userRepository.save(user);
            return "Profile image uploaded successfully.";
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    //  Remove Profile Image
    @PutMapping("/remove-image/{id}")
    public String removeProfileImage(@PathVariable int id) {
        return userRepository.findById(id).map(user -> {
            user.setProfileImage(null);
            userRepository.save(user);
            return "Profile image removed successfully.";
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}
