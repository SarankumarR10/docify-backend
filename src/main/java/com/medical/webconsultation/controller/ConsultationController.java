package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.Appointment;
import com.medical.webconsultation.model.Consultation;
import com.medical.webconsultation.model.User;
import com.medical.webconsultation.repository.AppointmentRepository;
import com.medical.webconsultation.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "http://localhost:3000")
public class ConsultationController {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // 1. Get all consultations (admin/debug use)
    @GetMapping
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    // 2. Create a new consultation with optional electronic signature
    @PostMapping("/create")
    public Consultation createConsultation(@RequestBody Consultation consultation) {
        int appointmentId = consultation.getAppointment().getId();

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

        if ((consultation.getNotes() == null || consultation.getNotes().trim().isEmpty()) &&
                (consultation.getPrescription() == null || consultation.getPrescription().trim().isEmpty())) {
            throw new RuntimeException("Either consultation notes or prescription must be provided.");
        }

        // Optional: Validate signature base64 (if needed)
        if (consultation.getSignature() != null && !consultation.getSignature().startsWith("data:image/")) {
            throw new RuntimeException("Invalid signature format. Signature must be a base64 image string.");
        }

        consultation.setAppointment(appointment);
        return consultationRepository.save(consultation);
    }

    // 3. Search consultations by patient name
    @GetMapping("/search")
    public List<Consultation> searchByPatientName(@RequestParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name parameter is required.");
        }
        return consultationRepository.findByAppointmentUserNameContainingIgnoreCase(name);
    }

    // 4. Get consultations by patient ID
    @GetMapping("/patient/{patientId}")
    public List<Consultation> getConsultationsByPatientId(@PathVariable int patientId) {
        return consultationRepository.findByAppointmentUserId(patientId);
    }

    // 5. Get all distinct patients who consulted a specific doctor
    @GetMapping("/doctor/{doctorId}/patients")
    public List<User> getPatientsByDoctor(@PathVariable int doctorId) {
        return consultationRepository.findDistinctPatientsByDoctorId(doctorId);
    }
}
