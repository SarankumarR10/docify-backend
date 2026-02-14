package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.Consultation;
import com.medical.webconsultation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    //  Search consultations by patient name (for filtering)
    List<Consultation> findByAppointmentUserNameContainingIgnoreCase(String name);

    //  Get consultations for a specific patient
    List<Consultation> findByAppointmentUserId(int userId);

    //  Get distinct patients for a given doctor (for doctor-side chat list)
    @Query("SELECT DISTINCT c.appointment.user FROM Consultation c WHERE c.appointment.doctor.id = :doctorId")
    List<User> findDistinctPatientsByDoctorId(@Param("doctorId") int doctorId);
}
