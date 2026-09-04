package com.service.PaymentService.Controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.service.PaymentService.DTO.PaymentRequestDto;
import com.service.PaymentService.Entity.Payment;
import com.service.PaymentService.Repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Transactional
public class PaymentController {

    @Value("${razorpay.key-id}")
    private   String razorpayKey;

    @Value("${razorpay.secret}")
    private  String razorpaySecret;

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    @Autowired
    private RestTemplate restTemplate;

    private  final PaymentRepository paymentRepository;


    @GetMapping("/health")
    public ResponseEntity<String> checkPaymentService(){
        return  ResponseEntity.ok("Request Reached payment-service");
    }


    @PostMapping("/create")
    public ResponseEntity<HashMap<String, String>> createOrder(@RequestBody PaymentRequestDto paymentRequest) {
        try {

log.info("Received payment request: {}", paymentRequest);
            Long orderId = paymentRequest.getOrderId();

           Payment paymentDetails =paymentRepository.findByOrderId(orderId);
            RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);


            if(paymentDetails==null) {

               log.info("Creating order with details: {}", paymentRequest);
               JSONObject orderRequestObj = new JSONObject();
               orderRequestObj.put("amount", ( paymentRequest.getAmount()) * 100); // Amount in paise
               orderRequestObj.put("currency", "INR");
               orderRequestObj.put("receipt", "txn_" + paymentRequest.getOrderId());

               Order order = razorpay.orders.create(orderRequestObj);

               Payment payment = new Payment();
               log.info("Saving payment details for order ID: {}", orderId);
                payment.setOrderId(orderId);
                payment.setPaymentId(order.get("id"));

                paymentRepository.save(payment);



               HashMap<String, String> response = new HashMap<>();
               response.put("orderId", order.get("id"));
               response.put("currency", order.get("currency"));
               response.put("amount", order.get("amount").toString());
               response.put("status", order.get("status"));
                return ResponseEntity.ok(response);
           }
            else{

                log.info("Fetching existing order with ID: {}", paymentDetails.getPaymentId());
                Order order=razorpay.orders.fetch(String.valueOf(paymentDetails.getPaymentId()));
                HashMap<String, String> response = new HashMap<>();
                response.put("orderId", order.get("id"));
                response.put("currency", order.get("currency"));
                response.put("amount", order.get("amount").toString());
                response.put("status", order.get("status"));
                return ResponseEntity.ok(response);
            }





        } catch (Exception e) {

            log.info(e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
        @PostMapping("/verify")
        public ResponseEntity<HashMap<String,Object>> verifyPayment(@RequestBody Map<String, String> request) {
            System.out.println(request);
            String paymentId = request.get("paymentId");
            String url = "https://api.razorpay.com/v1/payments/" + paymentId;

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("rzp_test_icxG7hFAniopGY", "Gu8tENaYx6QcKl3BfNhNNFRD");
            HttpEntity<String> entity = new HttpEntity<>(headers);
             try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                System.out.println(response.getBody());
                JSONObject jsonResponse = new JSONObject(response.getBody());
                 System.out.println(request.get("PlacedOrderId"));
                if ("captured".equals(jsonResponse.getString("status"))) {

                    ResponseEntity<String> message = restTemplate.postForEntity(
                            "http://localhost:8080/paymentupdate",
                            Map.of("orderId", request.get("PlacedOrderId")),
                            String.class
                    );
                    System.out.println(message.getBody());
                    HashMap<String,Object> h=new HashMap<>();
                    h.put("sucess",true);
                    h.put("response", message.getBody());
                    return ResponseEntity.ok(h);
                } else {
                    System.out.println("failed");
                }
            } catch (Exception e) {
                 HashMap<String,Object> h=new HashMap<>();
                 h.put("response", "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(h);
            }
            return null;
        }

}
