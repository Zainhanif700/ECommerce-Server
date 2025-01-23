package com.zain.controller;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.exception.SignatureVerificationException;
import com.zain.service.OrderService;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/payment")
public class StripeWebhookController {

    private String secretKey= "sk_test_51QYukKJ17QcbZNaeTtwp039lOYzgV560GWufxyyvrJTmYcexBwXuhxRZ7ZMN5Vt2q3itcc1eQcYpT38fmDfn5bcT00JjJkrMVE";

    private String endpointSecret = "whsec_esoe9jRnxS660rdE4fGVfyI1MLTZZrYM";

    private final OrderService orderService;

    public StripeWebhookController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = secretKey;

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Check if the event is of type 'checkout.session.completed'
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getData().getObject();
            String orderId = session.getMetadata().get("order_id");
            String paymentId = session.getPaymentIntent();

            // Update the payment status
            orderService.updatePaymentStatus(Long.parseLong(orderId), paymentId, "COMPLETED");
        }

        return ResponseEntity.ok("Webhook received");
    }
}
