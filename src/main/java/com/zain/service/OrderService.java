package com.zain.service;

import com.zain.exception.OrderException;
import com.zain.model.Address;
import com.zain.model.Order;
import com.zain.model.User;

import java.util.List;

public interface OrderService {

    public Order CreateOrder(User user, Address shippingAddress) throws OrderException;

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public Order placedOrder(Long orderID) throws OrderException;

    public Order confirmOrder(Long orderID) throws OrderException;

    public Order shippedOrder(Long orderID) throws OrderException;

    public Order deliveredOrder(Long orderID) throws OrderException;

    public Order cancelOrder(Long orderID) throws OrderException;

    public List<Order> getAllOrders() throws OrderException;

    public void deleteOrder(Long orderID) throws OrderException;

}
