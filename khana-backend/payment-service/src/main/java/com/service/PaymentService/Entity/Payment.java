package com.service.PaymentService.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @Column(name = "orderId", nullable = false)
    private Long orderId;

    @Column(name = "paymentId", nullable = false)
    private String paymentId;

    @Override
    public String toString() {
        return "paymentRequest{" +
                "orderId=" + orderId +
                ", paymentID='" + paymentId +
                '}';
    }
}
