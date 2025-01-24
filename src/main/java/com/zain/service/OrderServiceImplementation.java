package com.zain.service;

import com.zain.exception.OrderException;
import com.zain.exception.UserException;
import com.zain.model.*;
import com.zain.repository.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImplementation implements OrderService{

    private OrderItemRepository orderItemRepository;
    private OrderRepository orderRepository;
    private CartService cartService;
    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private OrderItemService orderItemService;

    @Autowired
    private EmailService emailService;

    public OrderServiceImplementation(OrderRepository orderRepository, CartService cartService, AddressRepository addressRepository, UserRepository userRepository, OrderItemService orderItemService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Order CreateOrder(User user, Address shippingAddress) throws OrderException {
        shippingAddress.setUser(user);
        Address shippingAddress1 = addressRepository.save(shippingAddress);
        user.getAddress().add(shippingAddress1);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItem = new ArrayList<>();
        for(CartItem item : cart.getCartItems()){
            OrderItem orderItem1 = new OrderItem();

            orderItem1.setPrice(item.getPrice());
            orderItem1.setProduct(item.getProduct());
            orderItem1.setQuantity(item.getQuantity());
            orderItem1.setSize(item.getSize());
            orderItem1.setUserId(item.getUserId());
            orderItem1.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createOrderItem = orderItemRepository.save(orderItem1);
            orderItem.add(createOrderItem);
        }

        Order createdOrder=new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItem(orderItem);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscounte(cart.getDiscounte());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setShippingAddress(shippingAddress1);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        createdOrder.setCreatedAt(LocalDateTime.now());
        Order savedOrder= orderRepository.save(createdOrder);
        for(OrderItem item: orderItem) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderID) throws OrderException {
        Optional<Order> opt = orderRepository.findById(orderID);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new OrderException("Order not exist with id "+ orderID);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        List<Order> orders = orderRepository.getUsersOrders(userId);
        return orders;
    }

    @Override
    public Order placedOrder(Long orderID) throws OrderException {
        Order order = findOrderById(orderID);
        order.setOrderStatus("PLACED");
        order.getPaymentDetails().setStatus("COMPLETED");
        return order;
    }

    @Override
    public Order confirmOrder(Long orderld) throws OrderException {
        Order order = findOrderById(orderld);
        ConfirmOrder(order);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderld) throws OrderException {
        Order order= findOrderById (orderld);
        OrderShipped(order);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderID) throws OrderException {
        Order order= findOrderById (orderID);
        OrderDelivered(order);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderID) throws OrderException {
        Order order= findOrderById (orderID);
        OrderCanceled(order);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() throws OrderException {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderID) throws OrderException {
        orderRepository.deleteById(orderID);
    }

    @Override
    public void updatePaymentStatus(Long orderId, String paymentId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.getPaymentDetails().setStatus(status);
        order.getPaymentDetails().setPaymentId(paymentId);
        order.setOrderStatus("CONFIRMED");
        order.setDeliveryDate(LocalDate.now().plusDays(7).atStartOfDay()); // Example delivery date

        orderRepository.save(order);
    }

    public void ConfirmOrder(Order order) throws OrderException, UserException {
        String subject = "Order Confirmation - Order #" + order.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Thank you for your purchase, " + order.getUser().getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">We are pleased to inform you that your order has been successfully placed.</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + order.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Order Date: " + order.getOrderDate() + "</p>"
                + "<p style=\"font-size: 16px;\">Delivery Date: " + order.getDeliveryDate() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + order.getShippingAddress().getStreetAddress() + ", "
                + order.getShippingAddress().getCity() + ", "
                + order.getShippingAddress().getState() + " "
                + order.getShippingAddress().getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">Total Price: $" + order.getTotalDiscountedPrice() + "</p>"
                + "<p style=\"font-size: 16px;\">Discounted Price: $" + (order.getTotalPrice()-order.getTotalDiscountedPrice()) + "</p>"
                + "<p style=\"font-size: 16px;\">Total Items: " + order.getTotalItem() + "</p>"
                + "<p style=\"font-size: 16px;\">Order Status: " + order.getOrderStatus() + "</p>"
                + "<p style=\"font-size: 16px;\">Your order will be shipped soon. We will notify you once it is on its way.</p>"
                + "<p style=\"font-size: 16px;\">If you have any questions or need further assistance, feel free to contact our support team.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(order.getUser().getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void OrderShipped(Order order) throws OrderException, UserException {
        String subject = "Order Shipped - Order #" + order.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Good news, " + order.getUser().getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">Your order has been shipped and is on its way!</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + order.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipped Date: " + order.getOrderDate() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + order.getShippingAddress().getStreetAddress() + ", "
                + order.getShippingAddress().getCity() + ", "
                + order.getShippingAddress().getState() + " "
                + order.getShippingAddress().getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">Track your shipment for real-time updates. We’ll notify you once it’s delivered.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(order.getUser().getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void OrderDelivered(Order order) throws OrderException, UserException {
        String subject = "Order Delivered - Order #" + order.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Hello, " + order.getUser().getUsername() + "!</h2>"
                + "<p style=\"font-size: 16px;\">We’re happy to inform you that your order has been delivered successfully!</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + order.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Delivered Date: " + order.getDeliveryDate() + "</p>"
                + "<p style=\"font-size: 16px;\">Shipping Address:</p>"
                + "<p style=\"font-size: 16px;\">" + order.getShippingAddress().getStreetAddress() + ", "
                + order.getShippingAddress().getCity() + ", "
                + order.getShippingAddress().getState() + " "
                + order.getShippingAddress().getZipCode() + "</p>"
                + "<p style=\"font-size: 16px;\">We hope you’re happy with your purchase. If you have any questions, feel free to contact us.</p>"
                + "<p style=\"font-size: 16px;\">Thank you for shopping with us!</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(order.getUser().getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void OrderCanceled(Order order) throws OrderException, UserException {
        String subject = "Order Canceled - Order #" + order.getId();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Dear " + order.getUser().getUsername() + ",</h2>"
                + "<p style=\"font-size: 16px;\">We regret to inform you that your order has been canceled.</p>"
                + "<p style=\"font-size: 16px;\">Order ID: " + order.getId() + "</p>"
                + "<p style=\"font-size: 16px;\">Cancellation Date: " + order.getCreatedAt() + "</p>"
                + "<p style=\"font-size: 16px;\">If you believe this was an error or have further questions, please contact our support team.</p>"
                + "<p style=\"font-size: 16px;\">We’re sorry for the inconvenience and hope to serve you better in the future.</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">Best regards,</p>"
                + "<p style=\"font-size: 16px; color: #007bff;\">LowTech GmbH</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(order.getUser().getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }



}
