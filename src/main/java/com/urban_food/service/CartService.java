package com.urban_food.service;

import com.urban_food.entity.Cart;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class CartService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall addToCartCall;
    private SimpleJdbcCall getAllCartsCall;
    private SimpleJdbcCall getCartByIdCall;
    private SimpleJdbcCall updateCartCall;
    private SimpleJdbcCall deleteCartCall;
    private SimpleJdbcCall checkoutCartCall;


    @PostConstruct
    public void init() {
        // Initialize the SimpleJdbcCall for Add to Cart Procedure
        addToCartCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("ADD_TO_CART_PROC")
                .declareParameters(
                        new SqlParameter("p_cartId", Types.VARCHAR),
                        new SqlParameter("p_customerId", Types.VARCHAR),
                        new SqlParameter("p_productId", Types.VARCHAR),
                        new SqlParameter("p_quantity", Types.INTEGER),
                        new SqlParameter("p_totalPrice", Types.NUMERIC)
                );
        checkoutCartCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("CHECKOUT_CART_PROC")
                .declareParameters(
                        new SqlParameter("p_cartID", Types.VARCHAR)
                );

        // Other procedure initializations
        getAllCartsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_ALL_CARTS_PROC")
                .returningResultSet("o_cursor", (rs, rowNum) -> mapCart(rs));

        getCartByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_CART_BY_ID_PROC")
                .declareParameters(new SqlParameter("p_cartId", Types.VARCHAR))
                .returningResultSet("o_cursor", (rs, rowNum) -> mapCart(rs));

        updateCartCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UPDATE_CART_PROC")
                .declareParameters(
                        new SqlParameter("p_cartId", Types.VARCHAR),
                        new SqlParameter("p_customerId", Types.VARCHAR),
                        new SqlParameter("p_productId", Types.VARCHAR),
                        new SqlParameter("p_quantity", Types.INTEGER)
                );

        deleteCartCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DELETE_CART_PROC")
                .declareParameters(new SqlParameter("p_cartId", Types.VARCHAR));
    }

    private Cart mapCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getString("cartId"));
        cart.setCustomerId(rs.getString("customerId"));
        cart.setProductId(rs.getString("productId"));
        cart.setQuantity(rs.getInt("quantity"));
        cart.setTotalPrice(rs.getBigDecimal("totalPrice"));
        return cart;
    }

    public void addToCart(Cart cart) {
        // Example logic for adding the cart item
        Map<String, Object> params = new HashMap<>();
        params.put("p_cartId", cart.getCartId());
        params.put("p_customerId", cart.getCustomerId());
        params.put("p_productId", cart.getProductId());
        params.put("p_quantity", cart.getQuantity());

        // Assuming totalPrice is calculated before calling this method
        BigDecimal totalPrice = calculateTotalPrice(cart.getProductId(), cart.getQuantity());
        params.put("p_totalPrice", totalPrice);

        // Execute the stored procedure or method to insert into DB
        addToCartCall.execute(params);
    }

    private BigDecimal calculateTotalPrice(String productId, int quantity) {
        // Logic to get price of product and calculate totalPrice
        BigDecimal pricePerUnit = getProductPrice(productId); // Fetch price from DB or service
        return pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal getProductPrice(String productId) {
        // Logic to get the product price from the database
        // Example: Fetch price from the Product table using the productId
        String query = "SELECT productPrice FROM Product WHERE productId = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{productId}, BigDecimal.class);
    }

    // Other methods remain the same, such as getting all carts, updating and deleting carts
    public List<Cart> getAllCarts() {
        Map<String, Object> result = getAllCartsCall.execute(new HashMap<>());
        return (List<Cart>) result.get("o_cursor");
    }

    public Optional<Cart> getCartById(String cartId) {
        Map<String, Object> result = getCartByIdCall.execute(Collections.singletonMap("p_cartId", cartId));
        List<Cart> list = (List<Cart>) result.get("o_cursor");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updateCart(Cart cart) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_cartId", cart.getCartId());
        params.put("p_customerId", cart.getCustomerId());
        params.put("p_productId", cart.getProductId());
        params.put("p_quantity", cart.getQuantity());
        updateCartCall.execute(params);
    }

    public void deleteCart(String cartId) {
        deleteCartCall.execute(Collections.singletonMap("p_cartId", cartId));
    }

    public void checkoutCart(String cartId) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_cartID", cartId);

        checkoutCartCall.execute(params);
    }

}
