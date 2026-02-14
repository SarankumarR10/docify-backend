package com.medical.webconsultation.repository;

import com.medical.webconsultation.model.MedicineOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineOrderRepository extends JpaRepository<MedicineOrder, Integer> {

    // Get all orders by a specific user
    List<MedicineOrder> findByUserId(int userId);

    // Get orders by status
    List<MedicineOrder> findByStatus(String status);

    // Get orders by user and status
    List<MedicineOrder> findByUserIdAndStatus(int userId, String status);
}
