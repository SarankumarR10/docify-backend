package com.medical.webconsultation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("docify17@gmail.com"); // optional if configured in properties

            mailSender.send(message);
            System.out.println("✅ Email sent successfully to: " + toEmail);
        } catch (MailException e) {
            System.err.println("❌ Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }
}
