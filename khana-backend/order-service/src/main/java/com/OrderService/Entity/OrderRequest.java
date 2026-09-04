package com.OrderService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @Id
    @Column(name = "id")
    private Long orderId;

    @Column(name = "userEmail", nullable = false)
    private String userEmail;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "status", nullable = false)
    private String orderStatus = "PENDING";


    @Column(name = "order_date")
    private Date orderDate = new Date();

    @Column(name="idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;



    @OneToMany(mappedBy = "orderRequest")
    private List<OrderItem> orderItems;

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orderId=" + orderId +
                ", userEmail='" + userEmail + '\'' +
                ", amount=" + amount +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderDate=" + orderDate +
                ", orderItems=" + orderItems +
                '}';
    }
}
