package com.example.shoppingcartapi.controller;

import com.example.shoppingcartapi.model.CartItem;
import com.example.shoppingcartapi.model.ShoppingCart;
import com.example.shoppingcartapi.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void testCreateCart() throws Exception {
        when(cartService.createCart(eq("test-user-id"))).thenReturn(testCart);

        mockMvc.perform(post("/api/carts")
                .param("userId", "test-user-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-cart-id"))
                .andExpect(jsonPath("$.userId").value("test-user-id"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.totalAmount").value(0.0))
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    public void testGetCart() throws Exception {
        when(cartService.getCart(eq("test-cart-id"))).thenReturn(testCart);

        mockMvc.perform(get("/api/carts/{cartId}", "test-cart-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-cart-id"))
                .andExpect(jsonPath("$.userId").value("test-user-id"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.totalAmount").value(0.0))
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    public void testGetCart_NotFound() throws Exception {
        when(cartService.getCart(eq("non-existent-cart-id")))
                .thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(get("/api/carts/{cartId}", "non-existent-cart-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testAddItemToCart() throws Exception {
        List<CartItem> items = new ArrayList<>();
        items.add(testItem);
        testCart.setItems(items);
        testCart.setTotalAmount(20.0);

        when(cartService.addItemToCart(eq("test-cart-id"), any(CartItem.class)))
                .thenReturn(testCart);

        mockMvc.perform(post("/api/carts/{cartId}/items", "test-cart-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-cart-id"))
                .andExpect(jsonPath("$.totalAmount").value(20.0))
                .andExpect(jsonPath("$.items.length()").value(1));
    }

    @Test
    public void testAddItemToCart_CartNotFound() throws Exception {
        when(cartService.addItemToCart(eq("non-existent-cart-id"), any(CartItem.class)))
                .thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(post("/api/carts/{cartId}/items", "non-existent-cart-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testItem)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testRemoveItemFromCart() throws Exception {
        when(cartService.removeItemFromCart(eq("test-cart-id"), eq("test-item-id")))
                .thenReturn(testCart);

        mockMvc.perform(delete("/api/carts/{cartId}/items/{itemId}", "test-cart-id", "test-item-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-cart-id"));
    }

    @Test
    public void testRemoveItemFromCart_CartNotFound() throws Exception {
        when(cartService.removeItemFromCart(eq("non-existent-cart-id"), eq("test-item-id")))
                .thenThrow(new RuntimeException("Cart not found"));

        mockMvc.perform(delete("/api/carts/{cartId}/items/{itemId}", "non-existent-cart-id", "test-item-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDeleteCart() throws Exception {
        doNothing().when(cartService).deleteCart(eq("test-cart-id"));

        mockMvc.perform(delete("/api/carts/{cartId}", "test-cart-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCart_CartNotFound() throws Exception {
        doNothing().when(cartService).deleteCart(eq("non-existent-cart-id"));

        mockMvc.perform(delete("/api/carts/{cartId}", "non-existent-cart-id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
} 