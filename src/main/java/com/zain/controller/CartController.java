package com.zain.controller;

import com.zain.Request.AddItemRequest;
import com.zain.exception.CartItemException;
import com.zain.exception.ProductException;
import com.zain.exception.UserException;
import com.zain.model.Cart;
import com.zain.model.User;
import com.zain.repository.CartItemRepository;
import com.zain.service.CartItemService;
import com.zain.service.CartService;
import com.zain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestHeader("Authorization") String jwt, @RequestBody AddItemRequest req) throws UserException, ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getId(),req);
        return ResponseEntity.ok("Cart Added Successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateItemToCart(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable long id,
                                                   @RequestBody Integer quantity) throws UserException, ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.updateCartItemById(id,quantity);
        return ResponseEntity.ok("Cart Updated Successfully");
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable long cartItemId,
                                                 @RequestHeader("Authorization") String jwt) throws  UserException, CartItemException{
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);
        return ResponseEntity.ok("Cart Deleted Successfully");
    }

}
