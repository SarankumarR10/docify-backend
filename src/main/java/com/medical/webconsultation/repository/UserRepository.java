package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.Role;
import com.medical.webconsultation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    User findByEmail(String email);

    // Find all users by their role using the Role enum
    List<User> findByRole(Role role);
}
