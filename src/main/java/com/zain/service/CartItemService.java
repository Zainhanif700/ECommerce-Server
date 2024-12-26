package com.zain.service;

import com.zain.exception.CartItemException;
import com.zain.exception.UserException;
import com.zain.model.Cart;
import com.zain.model.CartItem;
import com.zain.model.Product;

public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) throws CartItemException;
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    public CartItem findCartItemById(Long cartItemId) throws CartItemException;
    public CartItem updateCartItemById(Long cartItemId, Integer quantity) throws CartItemException;
}
