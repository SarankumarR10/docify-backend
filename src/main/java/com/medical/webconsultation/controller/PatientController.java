package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.Appointment;
import com.medical.webconsultation.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    //  Get all appointments for a patient by email
    @GetMapping("/{email}/appointments")
    public List<Appointment> getAppointmentsByPatientEmail(@PathVariable String email) {
        return appointmentRepository.findByUserEmail(email);
    }
}
