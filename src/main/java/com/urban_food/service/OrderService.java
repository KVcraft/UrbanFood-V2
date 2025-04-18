package com.urban_food.service;

import com.urban_food.entity.Order;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall insertOrderCall;
    private SimpleJdbcCall getAllOrdersCall;
    private SimpleJdbcCall getOrderByIdCall;
    private SimpleJdbcCall updateOrderCall;
    private SimpleJdbcCall deleteOrderCall;
    private SimpleJdbcCall getTotalSalesCall;
    private SimpleJdbcCall getTopProductsCall;

    @PostConstruct
    public void init() {
        insertOrderCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("InsertOrder");

        getAllOrdersCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetAllOrders")
                .returningResultSet("p_orders", this::mapOrder);

        getOrderByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetOrderById")
                .declareParameters(new SqlParameter("p_orderId", Types.VARCHAR))
                .returningResultSet("p_order", this::mapOrder);

        updateOrderCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UpdateOrder");

        deleteOrderCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteOrder");

        getTotalSalesCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetTotalSales")
                .declareParameters(
                        new SqlParameter("p_start_date", Types.DATE),
                        new SqlParameter("p_end_date", Types.DATE),
                        new SqlOutParameter("p_total", Types.NUMERIC)
                );

        getTopProductsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GetTopProducts")
                .returningResultSet("p_top_products", (rs, rowNum) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("product_id", rs.getString("product_id"));
                    map.put("name", rs.getString("name"));
                    map.put("times_ordered", rs.getInt("times_ordered"));
                    return map;
                });
    }

    private Order mapOrder(ResultSet rs, int rowNum) throws SQLException {
        return new Order(
                rs.getString("orderId"),
                rs.getString("productId"),
                rs.getInt("quantity"),
                rs.getDouble("totalAmount"),
                rs.getDate("orderDate"),
                rs.getString("customerID")
        );
    }

    public void createOrder(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_productId", order.getProductId());
        params.put("p_quantity", order.getQuantity());
        params.put("p_totalAmount", order.getTotalAmount());
        params.put("p_orderDate", new java.sql.Date(order.getOrderDate().getTime()));
        params.put("p_customerID", order.getCustomerID());
        insertOrderCall.execute(params);
    }

    public List<Order> getAllOrders() {
        Map<String, Object> result = getAllOrdersCall.execute();
        return (List<Order>) result.get("p_orders");
    }

    public Optional<Order> getOrderById(String orderId) {
        Map<String, Object> result = getOrderByIdCall.execute(Collections.singletonMap("p_orderId", orderId));
        List<Order> list = (List<Order>) result.get("p_order");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updateOrder(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_orderId", order.getOrderId());
        params.put("p_productId", order.getProductId());
        params.put("p_quantity", order.getQuantity());
        params.put("p_orderDate", new java.sql.Date(order.getOrderDate().getTime()));
        params.put("p_customerID", order.getCustomerID());
        updateOrderCall.execute(params);
    }

    public void deleteOrder(String orderId) {
        deleteOrderCall.execute(Collections.singletonMap("p_orderId", orderId));
    }

    public double getTotalSales(Date startDate, Date endDate) {
        Map<String, Object> result = getTotalSalesCall.execute(
                new java.sql.Date(startDate.getTime()),
                new java.sql.Date(endDate.getTime())
        );
        return result.get("p_total") != null ? ((Number) result.get("p_total")).doubleValue() : 0.0;
    }

    public List<Map<String, Object>> getTopProducts() {
        Map<String, Object> result = getTopProductsCall.execute();
        return (List<Map<String, Object>>) result.get("p_top_products");
    }
}
