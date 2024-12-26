package com.zain.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.exception.SignatureVerificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    // Webhook secret from Stripe (replace with your actual secret)
    private static final String WEBHOOK_SECRET = "whsec_c5c1e6dc51dffe75f7a3da652a2b3fd9f5457b2f2e7b77340ec046d24e142faf";

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, WEBHOOK_SECRET);
        } catch (SignatureVerificationException e) {
            System.err.println("Webhook signature verification failed: " + e.getMessage());
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }

        System.out.println("Received event: " + event.getType());

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getData().getObject();
            System.out.println(paymentIntent);
            System.out.println("PaymentIntent was successful! ID: " + paymentIntent.getId());
            System.out.println("Amount received: " + paymentIntent.getAmountReceived() / 100.0 + " USD");
            // You can now update your database with the payment intent information.

        } else {
            System.out.println("Unhandled event type: " + event.getType());
        }

        // Acknowledge receipt of the event
        return ResponseEntity.ok("Webhook received!");
    }
}
