package com.zain.controller;

import com.zain.exception.OrderException;
import com.zain.model.Order;
import com.zain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderControlle {
    @Autowired
    public OrderService orderService;

    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders() throws OrderException {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> ConfirmedOrderHandler(
            @PathVariable Long orderId, @RequestHeader("Authorization") String jwt
    ) throws OrderException {
        Order order = orderService.confirmOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> ShippeddOrderHandler(
            @PathVariable Long orderId, @RequestHeader("Authorization") String jwt
    ) throws OrderException {
        Order order = orderService.shippedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> DeliverOrderHandler(
            @PathVariable Long orderId, @RequestHeader("Authorization") String jwt
    ) throws OrderException {
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> CancelOrderHandler(
            @PathVariable Long orderId, @RequestHeader("Authorization") String jwt
    ) throws OrderException {
        Order order = orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<String> DeleteOrderHandler(
            @PathVariable Long orderId, @RequestHeader("Authorization") String jwt
    ) throws OrderException {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>("Order " + orderId + " Deleted Successfully", HttpStatus.OK);
    }
}
