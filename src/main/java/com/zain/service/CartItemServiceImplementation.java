package com.zain.service;

import com.zain.exception.CartItemException;
import com.zain.exception.UserException;
import com.zain.model.Cart;
import com.zain.model.CartItem;
import com.zain.model.Product;
import com.zain.model.User;
import com.zain.repository.CartItemRepository;
import com.zain.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImplementation implements CartItemService {

    private CartItemRepository cartItemRepository;
    private UserService userService;
    private CartRepository cartRepository;

    public CartItemServiceImplementation(CartItemRepository cartItemRepository, UserService userService, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());

        CartItem cartItemSaved = cartItemRepository.save(cartItem);
        return cartItemSaved;
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) throws CartItemException {
       CartItem cartItem = cartItemRepository.isCartItemExist(cart, product, size, userId);
       return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem item = findCartItemById(cartItemId);
        User user = userService.findUserById(item.getUserId());
        User requestedUser = userService.findUserById(userId);
        if (user.getId().equals(requestedUser.getId())) {
            cartItemRepository.deleteById(cartItemId);
        }
        else{
            throw new UserException("You Are Not Allowed To Do This Action");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()){
            return cartItem.get();
        }
        else{
            throw new CartItemException("CartItem Not Found");
        }
    }

    @Override
    public CartItem updateCartItemById(Long id, Integer quantity) throws CartItemException {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        if (cartItem.isPresent()){
            CartItem tempCart = cartItem.get();
            tempCart.setQuantity(quantity);
            return cartItemRepository.save(tempCart);
        }
        else{
            throw new CartItemException("CartItem Not Found");
        }
    }
}
