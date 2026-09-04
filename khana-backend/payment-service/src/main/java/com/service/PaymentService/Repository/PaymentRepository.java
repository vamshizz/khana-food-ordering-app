package com.service.PaymentService.Repository;


import com.service.PaymentService.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findByOrderId(Long orderId);
}
