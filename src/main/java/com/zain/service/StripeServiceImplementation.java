package com.zain.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.zain.Request.StripeProductRequest;
import com.zain.Response.StripeResponse;
import com.zain.model.User;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImplementation implements StripeService {
    @Value("${STRIPE.SKEY}")
    private String secreteKey;

    private final EmailService emailService;

    public StripeServiceImplementation(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public StripeResponse checkoutProducts(StripeProductRequest request, Integer orderId) {
        Stripe.apiKey = secreteKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(request.getName()).build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(request.getCurrency()==null?"USD":request.getCurrency())
                        .setUnitAmount(request.getAmount())
                        .setProductData(productData).build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(request.getQuantity())
                        .setPriceData(priceData).build();

        SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment/"+orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .putMetadata("order_id", String.valueOf(orderId)) // Include metadata
                .addLineItem(lineItem).build();

        Session session = null;
        try{
            session = Session.create(params);
        }
        catch (StripeException e){
            System.out.println(e.getMessage());
        }

        return StripeResponse
                .builder()
                .status("COMPLETED")
                .message("Payment Session created successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

}
