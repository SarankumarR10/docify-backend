package com.medical.webconsultation.controller;

import com.medical.webconsultation.model.MedicineOrder;
import com.medical.webconsultation.repository.MedicineOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicine-orders")
@CrossOrigin(origins = "http://localhost:3000") // Adjust if needed
public class MedicineOrderController {

    @Autowired
    private MedicineOrderRepository medicineOrderRepository;

    // Get all orders
    @GetMapping
    public List<MedicineOrder> getAllOrders() {
        return medicineOrderRepository.findAll();
    }

    // Place a new order with today's date
    @PostMapping
    public ResponseEntity<MedicineOrder> placeOrder(@RequestBody MedicineOrder order) {
        order.setOrderDate(LocalDate.now());
        order.setStatus("Pending");
        MedicineOrder savedOrder = medicineOrderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    // Update order status using JSON { "status": "Delivered" }
    @PutMapping("/{id}/status")
    public ResponseEntity<MedicineOrder> updateStatus(@PathVariable int id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return medicineOrderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    MedicineOrder updated = medicineOrderRepository.save(order);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get all orders by a user
    @GetMapping("/user/{userId}")
    public List<MedicineOrder> getOrdersByUser(@PathVariable int userId) {
        return medicineOrderRepository.findByUserId(userId);
    }
}
