package com.urban_food.service;

import com.urban_food.entity.Delivery;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall simpleJdbcCall;

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("delivery_pkg")
                .withProcedureName("get_all_deliveries")
                .declareParameters(
                        new SqlReturnResultSet("c_cursor", (rs, rowNum) -> mapDelivery(rs))
                );
    }

    public List<Delivery> getAllDeliveries() {
        Map<String, Object> result = simpleJdbcCall.execute();
        return (List<Delivery>) result.get("c_cursor");
    }

    private Delivery mapDelivery(ResultSet rs) throws SQLException {
        Delivery d = new Delivery();
        d.setDeliveryId(rs.getString("deliveryId"));
        d.setOrderId(rs.getString("orderId"));
        d.setDeliveryStatus(rs.getString("deliveryStatus"));
        d.setDeliveryAddress(rs.getString("deliveryAddress"));
        d.setDeliveryDate(rs.getDate("deliveryDate"));
        return d;
    }
}
