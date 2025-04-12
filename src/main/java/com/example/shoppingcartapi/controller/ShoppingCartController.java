package com.example.shoppingcartapi.controller;

import com.example.shoppingcartapi.model.CartItem;
import com.example.shoppingcartapi.model.ShoppingCart;
import com.example.shoppingcartapi.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class ShoppingCartController {
    
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);
    private final ShoppingCartService cartService;
    
    @Autowired
    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }
    
    @PostMapping
    public ResponseEntity<ShoppingCart> createCart(@RequestParam String userId) {
        logger.debug("Creating new cart for user: {}", userId);
        ShoppingCart cart = cartService.createCart(userId);
        logger.debug("Created cart with ID: {}", cart.getId());
        return ResponseEntity.ok(cart);
    }
    
    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCart> getCart(@PathVariable String cartId) {
        logger.debug("Fetching cart with ID: {}", cartId);
        ShoppingCart cart = cartService.getCart(cartId);
        logger.debug("Found cart: {}", cart);
        return ResponseEntity.ok(cart);
    }
    
    @PostMapping("/{cartId}/items")
    public ResponseEntity<ShoppingCart> addItemToCart(
            @PathVariable String cartId,
            @RequestBody CartItem item) {
        logger.debug("Adding item to cart {}: {}", cartId, item);
        ShoppingCart cart = cartService.addItemToCart(cartId, item);
        logger.debug("Updated cart total amount: {}", cart.getTotalAmount());
        return ResponseEntity.ok(cart);
    }
    
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<ShoppingCart> removeItemFromCart(
            @PathVariable String cartId,
            @PathVariable String itemId) {
        logger.debug("Removing item {} from cart {}", itemId, cartId);
        ShoppingCart cart = cartService.removeItemFromCart(cartId, itemId);
        logger.debug("Updated cart after item removal. Total amount: {}", cart.getTotalAmount());
        return ResponseEntity.ok(cart);
    }
    
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable String cartId) {
        logger.debug("Deleting cart with ID: {}", cartId);
        cartService.deleteCart(cartId);
        logger.debug("Cart {} deleted successfully", cartId);
        return ResponseEntity.ok().build();
    }
} 