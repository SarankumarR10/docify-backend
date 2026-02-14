package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.Doctor;
import com.medical.webconsultation.model.User;
import com.medical.webconsultation.repository.DoctorRepository;
import com.medical.webconsultation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // ✅ Get doctor by email
    @GetMapping("/by-email/{email}")
    public Doctor getDoctorByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            Doctor doctor = doctorRepository.findByUser(user);
            if (doctor != null) {
                doctor.setUser(user); // Ensure full user details
                return doctor;
            }
            Doctor emptyDoctor = new Doctor();
            emptyDoctor.setUser(user);
            return emptyDoctor;
        }
        throw new RuntimeException("User not found with email: " + email);
    }

    // ✅ Create doctor profile
    @PostMapping("/create")
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        if (doctor.getUser() == null || doctor.getUser().getId() == 0) {
            throw new RuntimeException("Doctor must be associated with a valid user.");
        }

        Doctor existing = doctorRepository.findByUser(doctor.getUser());
        if (existing != null) {
            throw new RuntimeException("Doctor profile already exists.");
        }

        return doctorRepository.save(doctor);
    }

    // ✅ Get doctor by user ID (used internally)
    @GetMapping("/user/{userId}")
    public Doctor getDoctorByUserId(@PathVariable int userId) {
        return doctorRepository.findByUserId(userId).orElse(null);
    }

    // ✅ Alias for frontend
    @GetMapping("/by-user/{userId}")
    public Doctor getDoctorByUserIdAlias(@PathVariable int userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found for user ID: " + userId));
    }

    // ✅ Update doctor profile (including registration info)
    @PutMapping("/update/{id}")
    public Doctor updateDoctor(@PathVariable int id, @RequestBody Doctor updatedDoctor) {
        return doctorRepository.findById(id).map(doctor -> {
            doctor.setSpecialization(updatedDoctor.getSpecialization());
            doctor.setExperience(updatedDoctor.getExperience());
            doctor.setQualifications(updatedDoctor.getQualifications());
            doctor.setLocation(updatedDoctor.getLocation());
            doctor.setAvailability(updatedDoctor.getAvailability());
            doctor.setProfileImage(updatedDoctor.getProfileImage());
            doctor.setRegistrationNumber(updatedDoctor.getRegistrationNumber());
            doctor.setRegistrationProof(updatedDoctor.getRegistrationProof());

            // Update user data if provided
            if (updatedDoctor.getUser() != null) {
                User existingUser = doctor.getUser();
                User updatedUser = updatedDoctor.getUser();

                if (updatedUser.getName() != null) existingUser.setName(updatedUser.getName());
                if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
                if (updatedUser.getPhone() != null) existingUser.setPhone(updatedUser.getPhone());

                userRepository.save(existingUser);
                doctor.setUser(existingUser);
            }

            return doctorRepository.save(doctor);
        }).orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    }

    // ✅ Delete doctor + cascade delete user
    @DeleteMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable int id) {
        return doctorRepository.findById(id).map(doctor -> {
            User user = doctor.getUser();
            userRepository.delete(user); // Triggers cascade delete of doctor
            return "Doctor and associated user deleted successfully.";
        }).orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    }

    // ✅ Upload profile image
    @PostMapping("/upload-image/{id}")
    public Doctor uploadProfileImage(@PathVariable int id, @RequestBody Doctor imageRequest) {
        return doctorRepository.findById(id).map(doctor -> {
            doctor.setProfileImage(imageRequest.getProfileImage());
            return doctorRepository.save(doctor);
        }).orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    }

    //  Remove profile image
    @PutMapping("/remove-image/{id}")
    public Doctor removeProfileImage(@PathVariable int id) {
        return doctorRepository.findById(id).map(doctor -> {
            doctor.setProfileImage(null);
            return doctorRepository.save(doctor);
        }).orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    }
}
