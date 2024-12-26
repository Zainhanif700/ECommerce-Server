package com.zain.model;

public class PaymentDetails {
    private String paymentMethod;
    private String status;
    private String paymentId;
    private String rayorpayPaymentLinkId;
    private String rayorpayPaymentReferenceId;
    private String rayorpayPaymentLinkStatus;
    private String rayorpayPaymentId;

    public PaymentDetails() {
    }

    public PaymentDetails(String paymentMethod, String status, String paymentId, String rayorpayPaymentLinkId, String rayorpayPaymentReferenceId, String rayorpayPaymentLinkStatus, String rayorpayPaymentId) {
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentId = paymentId;
        this.rayorpayPaymentLinkId = rayorpayPaymentLinkId;
        this.rayorpayPaymentReferenceId = rayorpayPaymentReferenceId;
        this.rayorpayPaymentLinkStatus = rayorpayPaymentLinkStatus;
        this.rayorpayPaymentId = rayorpayPaymentId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getRayorpayPaymentLinkId() {
        return rayorpayPaymentLinkId;
    }

    public void setRayorpayPaymentLinkId(String rayorpayPaymentLinkId) {
        this.rayorpayPaymentLinkId = rayorpayPaymentLinkId;
    }

    public String getRayorpayPaymentReferenceId() {
        return rayorpayPaymentReferenceId;
    }

    public void setRayorpayPaymentReferenceId(String rayorpayPaymentReferenceId) {
        this.rayorpayPaymentReferenceId = rayorpayPaymentReferenceId;
    }

    public String getRayorpayPaymentLinkStatus() {
        return rayorpayPaymentLinkStatus;
    }

    public void setRayorpayPaymentLinkStatus(String rayorpayPaymentLinkStatus) {
        this.rayorpayPaymentLinkStatus = rayorpayPaymentLinkStatus;
    }

    public String getRayorpayPaymentId() {
        return rayorpayPaymentId;
    }

    public void setRayorpayPaymentId(String rayorpayPaymentId) {
        this.rayorpayPaymentId = rayorpayPaymentId;
    }
}
