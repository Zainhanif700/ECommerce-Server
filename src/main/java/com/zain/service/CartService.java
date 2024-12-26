package com.zain.service;

import com.zain.Request.AddItemRequest;
import com.zain.exception.CartItemException;
import com.zain.exception.ProductException;
import com.zain.model.Cart;
import com.zain.model.CartItem;
import com.zain.model.User;

import java.util.Map;

public interface CartService {

    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, CartItemException;
    public Cart findUserCart(Long userId);
    public CartItem updateCartItem(CartItem cartItem, Integer req) throws CartItemException;
}
