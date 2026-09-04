package com.OrderService.Controller;


import com.OrderService.Client.PaymentClient;
import com.OrderService.DTO.OrderHistoryResponseDTO;
import com.OrderService.DTO.OrderItemDTO;
import com.OrderService.DTO.OrderRequestDto;
import com.OrderService.DTO.UpdatePaymentDTO;
import com.OrderService.Entity.OrderItem;
import com.OrderService.Entity.OrderRequest;
import com.OrderService.Entity.Restaurant;
import com.OrderService.Repository.MenuItemRepository;
import com.OrderService.Repository.OrderItemRepository;
import com.OrderService.Repository.OrderRequestRepository;
import com.OrderService.Repository.RestaurantRepository;
import com.OrderService.Service.CacheInspectService;
import com.OrderService.Service.OrderService;
import com.OrderService.Service.PaymentServiceClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {


    private final RestaurantRepository restaurantRepository;

    private final OrderService orderService;;

    private  final CacheInspectService CacheInspectService;

    private final OrderMapper orderMapper;

    private final OrderRequestRepository orderRequestRepository;

    private final OrderItemRepository orderItemRepository;

    private final MenuItemRepository menuItemRepository;

    private final RestTemplate restTemplate;

    private  final PaymentClient paymentClient;

    private  final PaymentServiceClient paymentServiceClient;



    @GetMapping("/health")
    public ResponseEntity<String> checkOrderService(){
        return  ResponseEntity.ok("Request Reached order-service");
    }


    @GetMapping("/addrestaurant")
    public ResponseEntity<String> saveResturantInfo() {

        return ResponseEntity.ok("rest added");
    }

    @GetMapping("food/restuarant")
    public ResponseEntity<List<Restaurant>> getRestaurants() {
        log.info("Fetching the resturants....");
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return ResponseEntity.ok(restaurantList);
    }

    @GetMapping("/food/restaurant/{id}")
    public  ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id){

        log.info("Fetching the resturant with id: {}", id);
        Restaurant restaurant=restaurantRepository.findById(id).orElseThrow(() -> new RuntimeException("Restaurant not found "));
        return  ResponseEntity.ok(restaurant);
    }


    @PostMapping("/food/createorder")
    @Transactional
    public ResponseEntity<HashMap<String,Object>> createOrder(@RequestHeader("Idempotency-Key") String idempotencyKey,@RequestBody OrderRequestDto orderRequestDto) {

    log.info("Received order creation request with idempotency key: {}", idempotencyKey);


    return  orderService.createOrder(orderRequestDto, idempotencyKey);

    }



    @PostMapping("/paymentupdate")
    @Transactional
    public ResponseEntity<String> updatePayment(@RequestBody UpdatePaymentDTO updatePaymentDTO){

        System.out.println(updatePaymentDTO);
        OrderRequest orderRequest = orderRequestRepository.findById(updatePaymentDTO.getOrderId()).orElseThrow(() -> new RuntimeException("OrderId not found "));
         orderRequest.setOrderStatus("Sucess");
        orderRequestRepository.save(orderRequest);
        return ResponseEntity.ok("Payment status updated successfully");

    }


//    @GetMapping("/orderhistory")
//    public ResponseEntity<List<OrderHistoryResponseDTO>> orderHistory(Long orderId) {
//
//        log.info("Recived order history request from user");
//
//
////          List<OrderHistoryResponseDTO> orderHistoryResponseDTOS= orderRequestRepository.findAllByUserEmail("csk123@gmail.com")
////                  .stream()
////                  .filter(x -> "Sucess".equals(x.getOrderStatus())) // Faster String comparison
////                  .map(x -> {
////                      List<OrderItemResponseDTO> items = x.getOrderItems()
////                              .stream()
////                              .map(y -> new OrderItemResponseDTO(y.getItemName(), y.getQuantity(), y.getPrice()))
////                              .toList();
////
////                      return new OrderHistoryResponseDTO(x.getAmount(), x.getOrderDate(), items);
////                  })
////                  .toList();
//        return ResponseEntity.ok(orderService.getOrderHistory("csk123@gmail.com"));
//    }


    @GetMapping("/orderhistory")
    public ResponseEntity<List<OrderHistoryResponseDTO>> orderHistory(@RequestParam int pageNo,@RequestParam int pageSize) {

        log.info("Recived order history request from user");

        return ResponseEntity.ok(orderService.getOrderHistory("csk123@gmail.com", PageRequest.of(pageNo,pageSize)));
    }




    @GetMapping("/getcache")
    public ResponseEntity<String> getCacheInfo() {
        // Call the service method to inspect the cache
        CacheInspectService.inspectCache("csk123@gmail.com");
        return ResponseEntity.ok("Cache inspection completed. Check logs for details.");
    }




    public static String generateOrderId() {
        String uuid = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        System.out.println(uuid);
        System.out.println("vamshi");// Keep only digits
        return uuid.substring(0, 10);
    }

    private Long generateNumericUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        return Long.parseLong(uuid, 16) % 10000000000L; // Convert to 10-digit number
    }

}
