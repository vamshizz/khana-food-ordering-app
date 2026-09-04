package com.OrderService.DTO;

 import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryResponseDTO {
    private  Integer amount;
    private Date orderDate ;
    private List<OrderItemResponseDTO> items;
}
