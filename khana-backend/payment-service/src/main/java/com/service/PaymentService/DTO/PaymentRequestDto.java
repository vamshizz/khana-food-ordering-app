package com.service.PaymentService.DTO;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long orderId;
    private Long amount;
}
