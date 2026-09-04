package com.OrderService.Controller;


import com.OrderService.DTO.OrderHistoryResponseDTO;
import com.OrderService.DTO.OrderItemResponseDTO;
import com.OrderService.Entity.OrderItem;
import com.OrderService.Entity.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring") // Auto-register with Spring
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "orderDate", target = "orderDate")
    OrderHistoryResponseDTO toOrderHistoryDTO(OrderRequest order);

    List<OrderHistoryResponseDTO> toOrderHistoryDTOList(List<OrderRequest> orders);

    @Mapping(source = "itemName", target = "itemName")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    OrderItemResponseDTO toOrderItemDTO(OrderItem orderItem);

    List<OrderItemResponseDTO> toOrderItemDTOList(List<OrderItem> orderItems);
}
