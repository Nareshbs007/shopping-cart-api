package com.example.shoppingcartapi.service;

import com.example.shoppingcartapi.exception.ResourceNotFoundException;
import com.example.shoppingcartapi.model.CartItem;
import com.example.shoppingcartapi.model.ShoppingCart;
import com.example.shoppingcartapi.repository.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository cartRepository;

    @InjectMocks
    private ShoppingCartService cartService;

    private ShoppingCart testCart;
    private CartItem testItem;

    @BeforeEach
    public void setUp() {
        testCart = new ShoppingCart();
        testCart.setId("test-cart-id");
        testCart.setUserId("test-user-id");
        testCart.setItems(new ArrayList<>());
        testCart.setTotalAmount(0.0);
        testCart.setStatus("ACTIVE");

        testItem = new CartItem();
        testItem.setId("test-item-id");
        testItem.setCartId("test-cart-id");
        testItem.setProductId("test-product-id");
        testItem.setProductName("Test Product");
        testItem.setPrice(10.0);
        testItem.setQuantity(2);
    }

    @Test
    public void testCreateCart() {
        when(cartRepository.save(any(ShoppingCart.class))).thenReturn(testCart);

        ShoppingCart result = cartService.createCart("test-user-id");

        assertNotNull(result);
        assertEquals("test-user-id", result.getUserId());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(0.0, result.getTotalAmount());
        assertTrue(result.getItems().isEmpty());

        verify(cartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddItemToCart() {
        when(cartRepository.findById("test-cart-id")).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(ShoppingCart.class))).thenReturn(testCart);

        ShoppingCart result = cartService.addItemToCart("test-cart-id", testItem);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(20.0, result.getTotalAmount()); // 10.0 * 2

        verify(cartRepository, times(1)).findById("test-cart-id");
        verify(cartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testAddItemToCart_CartNotFound() {
        when(cartRepository.findById("non-existent-cart-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.addItemToCart("non-existent-cart-id", testItem);
        });

        verify(cartRepository, times(1)).findById("non-existent-cart-id");
        verify(cartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    public void testRemoveItemFromCart() {
        List<CartItem> items = new ArrayList<>();
        items.add(testItem);
        testCart.setItems(items);
        testCart.setTotalAmount(20.0);

        when(cartRepository.findById("test-cart-id")).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(ShoppingCart.class))).thenReturn(testCart);

        ShoppingCart result = cartService.removeItemFromCart("test-cart-id", "test-item-id");

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());
        assertEquals(0.0, result.getTotalAmount());

        verify(cartRepository, times(1)).findById("test-cart-id");
        verify(cartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    public void testRemoveItemFromCart_CartNotFound() {
        when(cartRepository.findById("non-existent-cart-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.removeItemFromCart("non-existent-cart-id", "test-item-id");
        });

        verify(cartRepository, times(1)).findById("non-existent-cart-id");
        verify(cartRepository, never()).save(any(ShoppingCart.class));
    }

    @Test
    public void testGetCart() {
        when(cartRepository.findById("test-cart-id")).thenReturn(Optional.of(testCart));

        ShoppingCart result = cartService.getCart("test-cart-id");

        assertNotNull(result);
        assertEquals("test-cart-id", result.getId());
        assertEquals("test-user-id", result.getUserId());

        verify(cartRepository, times(1)).findById("test-cart-id");
    }

    @Test
    public void testGetCart_CartNotFound() {
        when(cartRepository.findById("non-existent-cart-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.getCart("non-existent-cart-id");
        });

        verify(cartRepository, times(1)).findById("non-existent-cart-id");
    }

    @Test
    public void testDeleteCart() {
        when(cartRepository.findById("test-cart-id")).thenReturn(Optional.of(testCart));
        doNothing().when(cartRepository).delete(any(ShoppingCart.class));

        cartService.deleteCart("test-cart-id");

        verify(cartRepository, times(1)).findById("test-cart-id");
        verify(cartRepository, times(1)).delete(testCart);
    }

    @Test
    public void testDeleteCart_CartNotFound() {
        when(cartRepository.findById("non-existent-cart-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.deleteCart("non-existent-cart-id");
        });

        verify(cartRepository, times(1)).findById("non-existent-cart-id");
        verify(cartRepository, never()).delete(any(ShoppingCart.class));
    }
} 