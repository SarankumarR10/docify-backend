package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Fetch by doctor ID
    List<Appointment> findByDoctorId(int doctorId);

    // Fetch by user (patient) ID
    List<Appointment> findByUserId(int userId);

    // Fetch by user email (optional use)
    List<Appointment> findByUserEmail(String email);

    // Fetch by doctor ID and status
    List<Appointment> findByDoctorIdAndStatus(int doctorId, String status);

    // Fetch by doctor ID and mode (Online/Offline)
    List<Appointment> findByDoctorIdAndModeIgnoreCase(int doctorId, String mode);

    // Fetch by user ID and mode (Online/Offline)
    List<Appointment> findByUserIdAndModeIgnoreCase(int userId, String mode);
}
