package com.zain.service;

import com.zain.Request.AddItemRequest;
import com.zain.exception.CartItemException;
import com.zain.exception.ProductException;
import com.zain.model.Cart;
import com.zain.model.CartItem;
import com.zain.model.Product;
import com.zain.model.User;
import com.zain.repository.CartItemRepository;
import com.zain.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartServiceImplementation implements CartService {

    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    CartItemService cartItemService;
    ProductService productService;

    public CartServiceImplementation(CartRepository cartRepository, CartItemService cartItemService, ProductService productService, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, CartItemException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(req.getProductId());

        CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
            int price = req.getQuantity() * product.getPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());
            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
            createdCartItem.setCart(cart);
            cartRepository.save(cart);
        }
        return "Item Added To Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscounte(totalPrice - totalDiscountedPrice);
        cart.setTotalDiscountedPrice(totalPrice - totalDiscountedPrice);
        return cartRepository.save(cart);
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem, Integer req) throws CartItemException {
        cartItem.setQuantity(req);
        cartItemRepository.save(cartItem);
        return cartItem;
    }
}
