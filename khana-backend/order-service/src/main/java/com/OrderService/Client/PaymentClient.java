package com.OrderService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.Map;

@FeignClient(name = "payment-service", url = "http://localhost:8082")
public interface PaymentClient {


    @PostMapping("/create")
    HashMap<String, String>  createOrder(Map<String, Object> paymentDetails);
}
