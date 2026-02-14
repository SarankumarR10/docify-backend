package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Integer> {
    List<HealthRecord> findByUserId(int userId);
    List<HealthRecord> findByDoctorId(int doctorId);
}
