package com.example.shoppingcartapi.service;

import com.example.shoppingcartapi.exception.BadRequestException;
import com.example.shoppingcartapi.exception.ResourceNotFoundException;
import com.example.shoppingcartapi.model.CartItem;
import com.example.shoppingcartapi.model.ShoppingCart;
import com.example.shoppingcartapi.repository.ShoppingCartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class ShoppingCartService {
    
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);
    private final ShoppingCartRepository cartRepository;
    
    @Autowired
    public ShoppingCartService(ShoppingCartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }
    
    public ShoppingCart createCart(String userId) {
        logger.debug("Creating new cart for user: {}", userId);
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("Invalid user ID provided: {}", userId);
            throw new BadRequestException("User ID cannot be empty");
        }
        
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        cart.setStatus("ACTIVE");
        cart.setTotalAmount(0.0);
        cart.setItems(new ArrayList<>());
        
        ShoppingCart savedCart = cartRepository.save(cart);
        logger.debug("Created new cart with ID: {}", savedCart.getId());
        return savedCart;
    }
    
    public ShoppingCart addItemToCart(String cartId, CartItem item) {
        logger.debug("Adding item to cart {}: {}", cartId, item);
        if (item == null) {
            logger.error("Null item provided for cart: {}", cartId);
            throw new BadRequestException("Cart item cannot be null");
        }
        if (item.getPrice() == null || item.getPrice() < 0) {
            logger.error("Invalid price {} for item in cart: {}", item.getPrice(), cartId);
            throw new BadRequestException("Invalid item price");
        }
        if (item.getQuantity() == null || item.getQuantity() < 1) {
            logger.error("Invalid quantity {} for item in cart: {}", item.getQuantity(), cartId);
            throw new BadRequestException("Invalid item quantity");
        }
        
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> {
                logger.error("Cart not found with ID: {}", cartId);
                return new ResourceNotFoundException("Cart not found with id: " + cartId);
            });
        
        item.setCartId(cartId);
        cart.getItems().add(item);
        
        // Update total amount
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item1 -> item1.getPrice() * item1.getQuantity())
                .sum();
        cart.setTotalAmount(totalAmount);
        
        ShoppingCart updatedCart = cartRepository.save(cart);
        logger.debug("Updated cart {} with new total amount: {}", cartId, totalAmount);
        return updatedCart;
    }
    
    public ShoppingCart removeItemFromCart(String cartId, String itemId) {
        logger.debug("Removing item {} from cart {}", itemId, cartId);
        if (itemId == null || itemId.trim().isEmpty()) {
            logger.error("Invalid item ID provided: {}", itemId);
            throw new BadRequestException("Item ID cannot be empty");
        }
        
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> {
                logger.error("Cart not found with ID: {}", cartId);
                return new ResourceNotFoundException("Cart not found with id: " + cartId);
            });
        
        boolean removed = cart.getItems().removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            logger.error("Item {} not found in cart {}", itemId, cartId);
            throw new ResourceNotFoundException("Item not found in cart with id: " + itemId);
        }
        
        // Update total amount
        double totalAmount = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        cart.setTotalAmount(totalAmount);
        
        ShoppingCart updatedCart = cartRepository.save(cart);
        logger.debug("Updated cart {} after item removal. New total amount: {}", cartId, totalAmount);
        return updatedCart;
    }
    
    public ShoppingCart getCart(String cartId) {
        logger.debug("Fetching cart with ID: {}", cartId);
        if (cartId == null || cartId.trim().isEmpty()) {
            logger.error("Invalid cart ID provided: {}", cartId);
            throw new BadRequestException("Cart ID cannot be empty");
        }
        
        return cartRepository.findById(cartId)
            .orElseThrow(() -> {
                logger.error("Cart not found with ID: {}", cartId);
                return new ResourceNotFoundException("Cart not found with id: " + cartId);
            });
    }
    
    public void deleteCart(String cartId) {
        logger.debug("Deleting cart with ID: {}", cartId);
        if (cartId == null || cartId.trim().isEmpty()) {
            logger.error("Invalid cart ID provided: {}", cartId);
            throw new BadRequestException("Cart ID cannot be empty");
        }
        
        ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> {
                logger.error("Cart not found with ID: {}", cartId);
                return new ResourceNotFoundException("Cart not found with id: " + cartId);
            });
        cartRepository.delete(cart);
        logger.debug("Cart {} deleted successfully", cartId);
    }
} 