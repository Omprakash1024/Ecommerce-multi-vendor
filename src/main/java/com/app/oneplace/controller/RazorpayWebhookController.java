package com.app.oneplace.controller;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.oneplace.model.Order;
import com.app.oneplace.model.PaymentOrder;
import com.app.oneplace.repo.OrderRepository;
import com.app.oneplace.repo.PaymentOrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Slf4j
public class RazorpayWebhookController {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;

    private final String webhookSecret = "your_webhook_secret_here"; // use the same secret you set in Razorpay
                                                                     // Dashboard

    @PostMapping("/razorpay")
    public ResponseEntity<String> handleRazorpayWebhook(@RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String razorpaySignature) {
        try {
            log.info("📨 Received Razorpay webhook: {}", payload);

            // Step 1: Validate webhook signature
            if (!verifyWebhookSignature(payload, razorpaySignature, webhookSecret)) {
                log.error("Webhook signature verification failed!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }

            // Step 2: Parse the event JSON
            JSONObject event = new JSONObject(payload);
            String eventType = event.getString("event");

            log.info("Razorpay event received: {}", eventType);

            // Step 3: Handle different events
            if (eventType.equals("payment.captured")) {
                String paymentId = event.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity")
                        .getString("id");
                log.info(" Payment captured for ID: {}", paymentId);

                // Now update your PaymentOrder and related Order statuses in DB
                PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentId);
                if (paymentOrder != null) {
                    paymentOrder.setStatus(com.app.oneplace.domain.PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);

                    for (Order order : paymentOrder.getOrders()) {
                        order.setPaymentStatus(com.app.oneplace.domain.PaymentStatus.COMPLETED);
                        orderRepository.save(order);
                    }
                    log.info("Payment and order records updated successfully.");
                } else {
                    log.warn("No payment order found for payment ID: {}", paymentId);
                }
            }

            // You can handle other event types like payment.failed here as well

            return ResponseEntity.ok("Webhook processed");

        } catch (Exception e) {
            log.error("Error handling webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Webhook error");
        }
    }

    private boolean verifyWebhookSignature(String payload, String actualSignature, String secret) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(payload.getBytes());
        String generatedSignature = Base64.getEncoder().encodeToString(hash);
        return generatedSignature.equals(actualSignature);
    }
}
