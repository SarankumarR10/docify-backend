package com.medical.webconsultation.controller;

import com.medical.webconsultation.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:3000")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String subject = request.get("subject");
        String body = request.get("body");

        try {
            emailService.sendEmail(to, subject, body);
            return "✅ Email sent successfully to " + to;
        } catch (Exception e) {
            return "❌ Failed to send email: " + e.getMessage();
        }
    }
}
