package com.zain.controller;

import com.zain.Request.ConfirmedOrder;
import com.zain.exception.OrderException;
import com.zain.exception.UserException;
import com.zain.model.Address;
import com.zain.model.Order;
import com.zain.model.User;
import com.zain.service.EmailService;
import com.zain.service.OrderService;
import com.zain.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/confirmOrder")
    public ResponseEntity<String> ConfirmOrder(@RequestBody ConfirmedOrder confirmedOrder) throws OrderException, UserException {
        System.out.println("Checking Confirmed Order");
        String subject = "Order Confirmation - Order #" + confirmedOrder.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Thank you for your purchase, " + confirmedOrder.getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">We are pleased to inform you that your order has been successfully placed.</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + confirmedOrder.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + confirmedOrder.getStreetAddress() + ", "
                + confirmedOrder.getCity() + ", " + confirmedOrder.getState() + " "
                + confirmedOrder.getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">Your order will be shipped soon. We will notify you once it is on its way.</p>"
                + "<p style=\"font-size: 16px;\">If you have any questions or need further assistance, feel free to contact our support team.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(confirmedOrder.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }

        // Return a success response to the client
        return new ResponseEntity<>("Order confirmation email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/shipped")
    public ResponseEntity<String> Shipped(@RequestBody ConfirmedOrder confirmedOrder) throws OrderException, UserException {

        String subject = "Your Order is Shipped - Order #" + confirmedOrder.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Good news, " + confirmedOrder.getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">Your order has been shipped and is on its way to you!</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + confirmedOrder.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + confirmedOrder.getStreetAddress() + ", "
                + confirmedOrder.getCity() + ", " + confirmedOrder.getState() + " "
                + confirmedOrder.getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">You will be notified when your package is out for delivery.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(confirmedOrder.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>("Shipped email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/outForDelivery")
    public ResponseEntity<String> OutForDelivery(@RequestBody ConfirmedOrder confirmedOrder) throws OrderException, UserException {

        String subject = "Your Order is Out for Delivery - Order #" + confirmedOrder.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Exciting News, " + confirmedOrder.getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">Your order is now out for delivery!</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + confirmedOrder.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + confirmedOrder.getStreetAddress() + ", "
                + confirmedOrder.getCity() + ", " + confirmedOrder.getState() + " "
                + confirmedOrder.getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">Your package should be with you soon! Keep an eye out for the delivery.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(confirmedOrder.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>("Out for Delivery email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/delivered")
    public ResponseEntity<String> Delivered(@RequestBody ConfirmedOrder confirmedOrder) throws OrderException, UserException {

        String subject = "Your Order is Delivered - Order #" + confirmedOrder.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Your Order has been Delivered, " + confirmedOrder.getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">We are happy to inform you that your order has been delivered!</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + confirmedOrder.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + confirmedOrder.getStreetAddress() + ", "
                + confirmedOrder.getCity() + ", " + confirmedOrder.getState() + " "
                + confirmedOrder.getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">We hope you enjoy your purchase! If you have any issues, feel free to contact us.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Your Store Team</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(confirmedOrder.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }

        return new ResponseEntity<>("Delivered email sent successfully", HttpStatus.OK);
    }


}
