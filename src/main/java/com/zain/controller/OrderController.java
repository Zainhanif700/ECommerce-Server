package com.zain.controller;

import com.zain.exception.OrderException;
import com.zain.exception.UserException;
import com.zain.model.Address;
import com.zain.model.Order;
import com.zain.model.User;
import com.zain.service.OrderService;
import com.zain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

   @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
       User user = userService.findUserProfileByJwt(jwt);
       Order order = orderService.CreateOrder(user, shippingAddress);
       return new ResponseEntity<>(order, HttpStatus.CREATED);
   }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> order = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<List<Order>>(order, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderbyId(@PathVariable("id") Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
 }
