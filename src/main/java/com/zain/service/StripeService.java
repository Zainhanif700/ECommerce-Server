package com.zain.service;

import com.zain.Request.StripeProductRequest;
import com.zain.Response.StripeResponse;

public interface StripeService {

    public StripeResponse checkoutProducts(StripeProductRequest request, Integer orderId);

}
