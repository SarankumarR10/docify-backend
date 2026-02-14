package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.Role;
import com.medical.webconsultation.model.User;
import com.medical.webconsultation.model.Doctor;
import com.medical.webconsultation.repository.DoctorRepository;
import com.medical.webconsultation.repository.UserRepository;
import com.medical.webconsultation.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    // Get all pending doctor registrations
    @GetMapping("/pending-doctors")
    public List<Doctor> getPendingDoctors() {
        return doctorRepository.findByUserRole(Role.pending_doctor);
    }

    // Approve doctor and send approval email
    @PutMapping("/approve-doctor/{userId}")
    public User approveDoctor(@PathVariable int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.doctor);
        User updatedUser = userRepository.save(user);

        // Send approval email safely
        try {
            String subject = "Doctor Account Approved ✅";
            String body = "Dear Dr. " + user.getName() + ",\n\n" +
                    "Your account has been successfully approved by the admin.\n" +
                    "You can now log in and access the doctor portal.\n\n" +
                    "Best regards,\nDocify Team";
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            System.err.println("❌ Failed to send approval email: " + e.getMessage());
        }

        return updatedUser;
    }

    //  Disapprove and delete doctor, then send rejection email
    @DeleteMapping("/delete-doctor/{doctorId}")
    public void deleteDoctor(@PathVariable int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = doctor.getUser();

        // Optional: Downgrade role to patient
        user.setRole(Role.patient);
        userRepository.save(user);

        doctorRepository.deleteById(doctorId);

        // Send rejection email safely
        try {
            String subject = "Doctor Application Disapproved ❌";
            String body = "Dear " + user.getName() + ",\n\n" +
                    "Unfortunately, your doctor application has been disapproved by the admin.\n" +
                    "Details that you are provided are Invalid, please provide valuable details .\n\n" +
                    "Best regards,\nDocify Team";
            emailService.sendEmail(user.getEmail(), subject, body);
        } catch (Exception e) {
            System.err.println("❌ Failed to send disapproval email: " + e.getMessage());
        }
    }
}
