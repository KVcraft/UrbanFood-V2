package com.urban_food.controller;

import com.urban_food.entity.Cart;
import com.urban_food.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // API to add a new cart item
    @PostMapping
    public ResponseEntity<Void> addToCart(@RequestBody Cart cart) {
        try {
            cartService.addToCart(cart);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // API to get all cart items
    @GetMapping
    public List<Cart> getAll() {
        return cartService.getAllCarts();
    }

    // API to get a specific cart item by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getById(@PathVariable String id) {
        return cartService.getCartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API to update a cart item by its ID
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Cart cart) {
        cart.setCartId(id);
        cartService.updateCart(cart);
        return ResponseEntity.ok().build();
    }

    // API to delete a cart item by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    // API to checkout the cart (simulate purchase)
    @PutMapping("/checkout/{cartId}")
    public ResponseEntity<Void> checkoutCart(@PathVariable String cartId) {
        try {
            cartService.checkoutCart(cartId);
            return ResponseEntity.ok().build(); // Return 200 OK if checkout successful
        } catch (Exception e) {
            e.printStackTrace();  // Print exception details in server logs
            return ResponseEntity.status(500).build(); // Return 500 Internal Server Error if exception occurs
        }
    }

}
