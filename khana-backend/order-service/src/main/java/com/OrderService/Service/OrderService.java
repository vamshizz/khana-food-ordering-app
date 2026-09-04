package com.OrderService.Service;


import com.OrderService.DTO.OrderHistoryResponseDTO;
import com.OrderService.DTO.OrderItemDTO;
import com.OrderService.DTO.OrderItemResponseDTO;
import com.OrderService.DTO.OrderRequestDto;
import com.OrderService.Entity.OrderItem;
import com.OrderService.Entity.OrderRequest;
import com.OrderService.Repository.OrderItemRepository;
import com.OrderService.Repository.OrderRequestRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final OrderRequestRepository orderRequestRepository;
    private final OrderItemRepository orderItemRepository;
    private  final PaymentServiceClient paymentServiceClient;

    public OrderService(OrderRequestRepository orderRequestRepository, OrderItemRepository orderItemRepository, PaymentServiceClient paymentServiceClient) {
        this.orderRequestRepository = orderRequestRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentServiceClient = paymentServiceClient;
    }


    public   ResponseEntity<HashMap<String,Object>> createOrder(OrderRequestDto orderRequestDto, String idempotencyKey) {

        OrderRequest record = orderRequestRepository.findByIdempotencyKey(idempotencyKey);


        if (record == null) {

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setOrderId(generateNumericUUID());
            orderRequest.setUserEmail(orderRequestDto.getUserEmail());
            orderRequest.setIdempotencyKey(idempotencyKey);
            // orderRequest.setAmount(orderRequestDto.getAmount());

            int amount = orderRequestDto.getItems().stream().mapToInt(OrderItemDTO::getTotalPrice).sum();
            orderRequest.setAmount(amount + 50);


            OrderRequest orderRequest1 = orderRequestRepository.save(orderRequest);


            List<OrderItem> savedItems = orderRequestDto.getItems().stream().map(x -> {

                OrderItem orderItem = new OrderItem();
                orderItem.setItemName(x.getItemname());
                orderItem.setPrice(x.getTotalPrice());
                orderItem.setQuantity(x.getQuantity());
                orderItem.setOrderRequest(orderRequest1);
                return orderItem;
            }).toList();

            orderItemRepository.saveAll(savedItems);

        }

         record= orderRequestRepository.findByIdempotencyKey(idempotencyKey);

        if (record.getOrderStatus().equals("PENDING")) {
            HashMap<String, Object> paymentRequest = new HashMap<>();
            paymentRequest.put("amount", record.getAmount());
            paymentRequest.put("orderId", record.getOrderId());


            // Use Feign client to process payment and handle response
            HashMap<String, String> paymentResponse = null;

            paymentResponse = paymentServiceClient.createPayment(paymentRequest);
            log.info("Payment response received: {}", paymentResponse);


            HashMap<String, Object> responseData = new HashMap<>();

            if (paymentResponse.get("status").equals("SERVICE_UNAVAILABLE")) {
                log.error("Payment service is unavailable. Order creation failed.");
                responseData.put("error", "Payment service is temporarily unavailable. Please try again later.");
                return ResponseEntity.status(503).body(responseData);
            }

            responseData.put("orderId", record.getOrderId());
            responseData.put("paymentDetails", paymentResponse);
            return ResponseEntity.ok(responseData);

        }

        return  ResponseEntity.status(500).body(new HashMap<>() {{
            put("error", "INTERNAL_SERVER_ERROR");
        }});
    }

    @Cacheable(value = "orderHistory", key = "#userEmail")
    public List<OrderHistoryResponseDTO> getOrderHistory(String userEmail, Pageable pageable) {

        log.info("CACHE MISS — hitting DB for: {}", userEmail);

       return orderRequestRepository.findAllByUserEmail("csk123@gmail.com", pageable)
               .getContent()
                .stream()


                .filter(x -> "Sucess".equals(x.getOrderStatus())) // Faster String comparison
                .map(x -> {
                    List<OrderItemResponseDTO> items = x.getOrderItems()
                            .stream()
                            .map(y -> new OrderItemResponseDTO(y.getItemName(), y.getQuantity(), y.getPrice()))
                            .toList();

                    return new OrderHistoryResponseDTO(x.getAmount(), x.getOrderDate(), items);
                })
                .toList();


    }


    private Long generateNumericUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        return Long.parseLong(uuid, 16) % 10000000000L; // Convert to 10-digit number
    }

}
