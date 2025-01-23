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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/stripe")
public class StripeWebhookController {

    private String secretKey= "sk_test_51QYukKJ17QcbZNaeTtwp039lOYzgV560GWufxyyvrJTmYcexBwXuhxRZ7ZMN5Vt2q3itcc1eQcYpT38fmDfn5bcT00JjJkrMVE";

    private String endpointSecret = "whsec_esoe9jRnxS660rdE4fGVfyI1MLTZZrYM";



    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = secretKey;
        System.out.println("Received payload: " + payload);

        Event event;
        try {
            // Verify the Stripe signature and construct the event
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Check if the event is of type 'checkout.session.completed'
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getData().getObject();
            String orderId = session.getMetadata().get("order_id");

            // Validate if the orderId is present
            if (orderId == null || orderId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing orderId in metadata");
            }

            try {
                // Attempt to parse the orderId
                Long parsedOrderId = Long.parseLong(orderId);
                String paymentId = session.getPaymentIntent();

                System.out.println("Payment successful: Order ID: " + parsedOrderId + ", Payment ID: " + paymentId);
                // Update the payment status
            } catch (NumberFormatException e) {
                // If orderId is not a valid number, return a bad request response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid orderId format: " + orderId);
            }
        }

        return ResponseEntity.ok("Webhook received");
    }

}
