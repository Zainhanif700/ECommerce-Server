package com.zain.service;

import com.zain.exception.OrderException;
import com.zain.model.*;
import com.zain.repository.*;
import org.springframework.stereotype.Service;

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
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderld) throws OrderException {
        Order order= findOrderById (orderld);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderID) throws OrderException {
        Order order= findOrderById (orderID);
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderID) throws OrderException {
        Order order= findOrderById (orderID);
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
}
