package com.example.shoppingcartapi.repository;

import com.example.shoppingcartapi.model.CartItem;
import com.example.shoppingcartapi.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ShoppingCartRepositoryIntegrationTest {

    @Autowired
    private ShoppingCartRepository cartRepository;

    private ShoppingCart testCart;
    private CartItem testItem;

    @BeforeEach
    public void setUp() {
        testCart = new ShoppingCart();
        testCart.setUserId("test-user-id");
        testCart.setItems(new ArrayList<>());
        testCart.setTotalAmount(0.0);
        testCart.setStatus("ACTIVE");

        testItem = new CartItem();
        testItem.setProductId("test-product-id");
        testItem.setProductName("Test Product");
        testItem.setPrice(10.0);
        testItem.setQuantity(2);
    }

    @Test
    public void testSaveAndFindById() {
        // Save the cart
        ShoppingCart savedCart = cartRepository.save(testCart);
        assertNotNull(savedCart);
        assertNotNull(savedCart.getId());

        // Find the cart by ID
        Optional<ShoppingCart> foundCartOpt = cartRepository.findById(savedCart.getId());
        assertTrue(foundCartOpt.isPresent());
        ShoppingCart foundCart = foundCartOpt.get();
        assertEquals(savedCart.getId(), foundCart.getId());
        assertEquals(testCart.getUserId(), foundCart.getUserId());
        assertEquals(testCart.getStatus(), foundCart.getStatus());
        assertEquals(testCart.getTotalAmount(), foundCart.getTotalAmount());
        assertTrue(foundCart.getItems().isEmpty());
    }

    @Test
    public void testSaveWithItems() {
        // Add item to cart
        List<CartItem> items = new ArrayList<>();
        items.add(testItem);
        testCart.setItems(items);
        testCart.setTotalAmount(20.0);

        // Save the cart with items
        ShoppingCart savedCart = cartRepository.save(testCart);
        assertNotNull(savedCart);
        assertEquals(1, savedCart.getItems().size());
        assertEquals(20.0, savedCart.getTotalAmount());

        // Find the cart by ID
        Optional<ShoppingCart> foundCartOpt = cartRepository.findById(savedCart.getId());
        assertTrue(foundCartOpt.isPresent());
        ShoppingCart foundCart = foundCartOpt.get();
        assertEquals(1, foundCart.getItems().size());
        assertEquals(20.0, foundCart.getTotalAmount());
    }

    @Test
    public void testDelete() {
        // Save the cart
        ShoppingCart savedCart = cartRepository.save(testCart);
        assertNotNull(savedCart);

        // Delete the cart
        cartRepository.delete(savedCart);

        // Try to find the deleted cart
        Optional<ShoppingCart> deletedCart = cartRepository.findById(savedCart.getId());
        assertFalse(deletedCart.isPresent());
    }
} 