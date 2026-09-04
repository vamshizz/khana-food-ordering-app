package com.OrderService.Service;

import com.OrderService.Client.PaymentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceClient {

    private final PaymentClient paymentClient;

    private static final String PAYMENT_SERVICE = "paymentService";

    int attempt=1;
    @Retry(name = PAYMENT_SERVICE)
    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public HashMap<String, String> createPayment(Map<String, Object> paymentRequest) {

        log.info("Calling payment-service");


        return paymentClient.createOrder(paymentRequest);
    }

    public HashMap<String, String> paymentFallback(
            Map<String, Object> paymentRequest,
            Throwable throwable) {

        log.error("Payment service unavailable. Fallback triggered: {}",
                throwable.getMessage());

        HashMap<String, String> fallbackResponse = new HashMap<>();

        fallbackResponse.put("status", "SERVICE_UNAVAILABLE");
        fallbackResponse.put("message",
                "Payment service is temporarily unavailable");

        return fallbackResponse;
    }


}
