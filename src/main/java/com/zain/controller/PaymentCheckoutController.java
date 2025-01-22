package com.zain.controller;

import com.zain.Request.StripeProductRequest;
import com.zain.Response.StripeResponse;
import com.zain.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentCheckoutController {
    @Autowired
    private StripeService stripeService;

    @PostMapping("/{orderId}")
    public ResponseEntity<StripeResponse> checkoutProducts(@PathVariable Integer orderId,  @RequestBody StripeProductRequest stripeProductRequest) {
        StripeResponse stripeResponse = stripeService.checkoutProducts(stripeProductRequest, orderId);
        return ResponseEntity.ok(stripeResponse);
    }
}
