package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.Doctor;
import com.medical.webconsultation.model.Role;
import com.medical.webconsultation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Doctor findByUser(User user);

    Optional<Doctor> findByUserId(int userId);

    //  Fetch doctors where the associated user's role is 'pending_doctor'
    @Query("SELECT d FROM Doctor d WHERE d.user.role = :role")
    List<Doctor> findByUserRole(Role role);
}
