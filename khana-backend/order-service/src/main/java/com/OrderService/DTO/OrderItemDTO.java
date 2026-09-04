package com.OrderService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private  Integer itemId;
    private  String itemname;
    private  Integer quantity;
    private  Integer unitPrice;
    private  Integer totalPrice;
}
