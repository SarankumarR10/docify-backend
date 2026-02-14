package com.medical.webconsultation.model;

import jakarta.persistence.*;

@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Each consultation is linked to a specific appointment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    // Long text field to store consultation notes
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Long text field to store prescription details
    @Column(columnDefinition = "TEXT")
    private String prescription;

    // Long text field to store base64 signature image
    @Column(columnDefinition = "LONGTEXT")
    private String signature;

    // ===== Getters and Setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
