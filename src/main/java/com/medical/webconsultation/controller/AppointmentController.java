package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.Appointment;
import com.medical.webconsultation.model.Doctor;
import com.medical.webconsultation.model.User;
import com.medical.webconsultation.repository.AppointmentRepository;
import com.medical.webconsultation.repository.DoctorRepository;
import com.medical.webconsultation.repository.UserRepository;
import com.medical.webconsultation.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    // Get appointments by doctor ID
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> getAppointmentsByDoctorId(@PathVariable int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Get appointments by user (patient) ID
    @GetMapping("/user/{userId}")
    public List<Appointment> getAppointmentsByUserId(@PathVariable int userId) {
        return appointmentRepository.findByUserId(userId);
    }

    // Create appointment and send email to doctor
    @PostMapping("/create")
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        // Fetch full doctor and user from DB
        Doctor fullDoctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User fullUser = userRepository.findById(appointment.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        appointment.setDoctor(fullDoctor);
        appointment.setUser(fullUser);

        Appointment saved = appointmentRepository.save(appointment);

        try {
            String doctorEmail = fullDoctor.getUser().getEmail();
            String doctorName = fullDoctor.getUser().getName();
            String patientName = fullUser.getName();

            String subject = "📅 New Appointment Booked by " + patientName;
            String body = "Dear Dr. " + doctorName + ",\n\n"
                    + "A new appointment has been booked by " + patientName + ".\n\n"
                    + "📅 Date: " + saved.getDate() + "\n"
                    + "⏰ Time: " + saved.getTime() + "\n"
                    + "💬 Reason: " + saved.getReason() + "\n"
                    + "📍 Mode: " + saved.getMode() + "\n\n"
                    + "Please log in to your dashboard to approve or reject the appointment.\n\n"
                    + "Regards,\nDocify Team";

            emailService.sendEmail(doctorEmail, subject, body);

        } catch (Exception e) {
            System.err.println("❌ Failed to send appointment email to doctor: " + e.getMessage());
        }

        return saved;
    }

    // Update appointment status and notify on Approved or Cancelled
    @PutMapping("/update-status/{id}")
    public ResponseEntity<Appointment> updateStatus(@PathVariable int id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        if (newStatus == null || newStatus.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return appointmentRepository.findById(id)
                .map(appointment -> {
                    appointment.setStatus(newStatus);
                    Appointment updated = appointmentRepository.save(appointment);

                    // Only send email on Approved or Cancelled
                    if (newStatus.equalsIgnoreCase("Approved") || newStatus.equalsIgnoreCase("Cancelled")) {
                        try {
                            User patient = updated.getUser();
                            User doctor = updated.getDoctor().getUser();

                            String subject = "";
                            String body = "";

                            if ("Approved".equalsIgnoreCase(newStatus)) {
                                subject = "✅ Appointment Approved";
                                body = "Dear " + patient.getName() + ",\n\n"
                                        + "Your appointment with Dr. " + doctor.getName() + " has been approved.\n\n"
                                        + "📅 Date: " + updated.getDate() + "\n"
                                        + "⏰ Time: " + updated.getTime() + "\n"
                                        + "📍 Mode: " + updated.getMode() + "\n\n"
                                        + "Please be available on time.\n\n"
                                        + "Regards,\nDocify Team";
                            } else if ("Cancelled".equalsIgnoreCase(newStatus)) {
                                subject = "❌ Appointment Cancelled";
                                body = "Dear " + patient.getName() + ",\n\n"
                                        + "We regret to inform you that your appointment with Dr. " + doctor.getName()
                                        + " has been cancelled.\n\n"
                                        + "Regards,\nDocify Team";
                            }

                            emailService.sendEmail(patient.getEmail(), subject, body);
                        } catch (Exception e) {
                            System.err.println("❌ Failed to send status update email: " + e.getMessage());
                        }
                    }

                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Direct string-based update (not recommended)
    @PutMapping("/{id}/status")
    public Appointment updateAppointmentStatus(@PathVariable int id, @RequestBody String status) {
        return appointmentRepository.findById(id).map(appt -> {
            appt.setStatus(status);
            return appointmentRepository.save(appt);
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public String deleteAppointment(@PathVariable int id) {
        return appointmentRepository.findById(id).map(appt -> {
            appointmentRepository.delete(appt);
            return "Appointment deleted";
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // Get appointments by doctor and mode
    @GetMapping("/doctor/{doctorId}/mode/{mode}")
    public List<Appointment> getAppointmentsByDoctorIdAndMode(@PathVariable int doctorId, @PathVariable String mode) {
        return appointmentRepository.findByDoctorIdAndModeIgnoreCase(doctorId, mode);
    }

    // Get appointments by patient and mode
    @GetMapping("/user/{userId}/mode/{mode}")
    public List<Appointment> getAppointmentsByUserIdAndMode(@PathVariable int userId, @PathVariable String mode) {
        return appointmentRepository.findByUserIdAndModeIgnoreCase(userId, mode);
    }
}
