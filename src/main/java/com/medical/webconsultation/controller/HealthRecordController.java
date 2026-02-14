package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.Appointment;
import com.medical.webconsultation.model.HealthRecord;
import com.medical.webconsultation.repository.AppointmentRepository;
import com.medical.webconsultation.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthRecordController {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // ✅ Get records visible to a specific doctor (Approved/Completed appointments only)
    @GetMapping("/doctor/{doctorId}")
    public List<HealthRecord> getRecordsByDoctorId(@PathVariable int doctorId) {
        // Find user IDs who have appointments with this doctor with Approved/Completed status
        Set<Integer> userIdsWithValidAppointments = appointmentRepository.findByDoctorId(doctorId).stream()
                .filter(a -> a.getStatus().equalsIgnoreCase("Approved") || a.getStatus().equalsIgnoreCase("Completed"))
                .map(a -> a.getUser().getId())
                .collect(Collectors.toSet());

        // Return only health records belonging to those users
        return healthRecordRepository.findAll().stream()
                .filter(record -> userIdsWithValidAppointments.contains(record.getUser().getId()))
                .collect(Collectors.toList());
    }

    // ✅ Other endpoints unchanged
    @GetMapping("/user/{userId}")
    public List<HealthRecord> getRecordsByUserId(@PathVariable int userId) {
        return healthRecordRepository.findByUserId(userId);
    }

    @PostMapping("/upload")
    public HealthRecord uploadRecord(@RequestBody HealthRecord record) {
        return healthRecordRepository.save(record);
    }

    @GetMapping("/{id}")
    public HealthRecord getRecordById(@PathVariable int id) {
        return healthRecordRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRecord(@PathVariable int id) {
        return healthRecordRepository.findById(id).map(record -> {
            healthRecordRepository.delete(record);
            return "Health record deleted successfully.";
        }).orElse("Record not found with ID: " + id);
    }
}
