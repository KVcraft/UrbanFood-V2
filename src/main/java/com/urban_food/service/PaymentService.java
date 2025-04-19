package com.urban_food.service;

import com.urban_food.entity.Payment;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall insertPaymentCall;
    private SimpleJdbcCall getAllPaymentsCall;
    private SimpleJdbcCall getPaymentByIdCall;
    private SimpleJdbcCall updatePaymentCall;
    private SimpleJdbcCall deletePaymentCall;

    @PostConstruct
    public void init() {
        insertPaymentCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("INSERT_PAYMENT")
                .declareParameters(
                        new SqlParameter("p_payment_id", Types.VARCHAR),
                        new SqlParameter("p_status", Types.VARCHAR),
                        new SqlParameter("p_method", Types.VARCHAR),
                        new SqlParameter("p_date", Types.DATE),
                        new SqlParameter("p_customer_id", Types.VARCHAR),
                        new SqlParameter("p_order_id", Types.VARCHAR)
                );

        getAllPaymentsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SELECT_ALL_PAYMENTS")
                .returningResultSet("o_cursor", this::mapPayment);

        getPaymentByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("SELECT_PAYMENT_BY_ID")
                .declareParameters(new SqlParameter("p_payment_id", Types.VARCHAR))
                .returningResultSet("o_cursor", this::mapPayment);

        updatePaymentCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UPDATE_PAYMENT")
                .declareParameters(
                        new SqlParameter("p_payment_id", Types.VARCHAR),
                        new SqlParameter("p_status", Types.VARCHAR),
                        new SqlParameter("p_method", Types.VARCHAR),
                        new SqlParameter("p_date", Types.DATE),
                        new SqlParameter("p_customer_id", Types.VARCHAR),
                        new SqlParameter("p_order_id", Types.VARCHAR)
                );

        deletePaymentCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DELETE_PAYMENT")
                .declareParameters(new SqlParameter("p_payment_id", Types.VARCHAR));
    }

    private Payment mapPayment(ResultSet rs, int rowNum) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getString("paymentId"));
        payment.setStatus(rs.getString("status"));
        payment.setPaymentMethod(rs.getString("paymentMethod"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setPaymentDate(rs.getDate("paymentDate"));
        payment.setCustomerID(rs.getString("customerID"));
        payment.setOrderId(rs.getString("orderId"));
        return payment;
    }

    public void insertPayment(Payment payment) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_payment_id", payment.getPaymentId());
        params.put("p_status", payment.getStatus());
        params.put("p_method", payment.getPaymentMethod());
        params.put("p_date", new java.sql.Date(payment.getPaymentDate().getTime()));
        params.put("p_customer_id", payment.getCustomerID());
        params.put("p_order_id", payment.getOrderId());
        insertPaymentCall.execute(params);
    }

    public List<Payment> getAllPayments() {
        Map<String, Object> result = getAllPaymentsCall.execute(new HashMap<>());
        return (List<Payment>) result.get("o_cursor");
    }

    public Optional<Payment> getPaymentById(String id) {
        Map<String, Object> result = getPaymentByIdCall.execute(Collections.singletonMap("p_payment_id", id));
        List<Payment> list = (List<Payment>) result.get("o_cursor");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updatePayment(Payment payment) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_payment_id", payment.getPaymentId());
        params.put("p_status", payment.getStatus());
        params.put("p_method", payment.getPaymentMethod());
        params.put("p_date", new java.sql.Date(payment.getPaymentDate().getTime()));
        params.put("p_customer_id", payment.getCustomerID());
        params.put("p_order_id", payment.getOrderId());
        updatePaymentCall.execute(params);
    }

    public void deletePayment(String id) {
        deletePaymentCall.execute(Collections.singletonMap("p_payment_id", id));
    }
}
