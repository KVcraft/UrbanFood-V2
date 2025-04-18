package com.urban_food.service;

import com.urban_food.entity.Customer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall simpleJdbcCall;

    @PostConstruct
    public void init() {
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("customer_pkg")
                .withProcedureName("get_all_customers")
                .declareParameters(
                        new SqlReturnResultSet("c_cursor", (rs, rowNum) -> mapCustomer(rs))
                );
    }

    public List<Customer> getAllCustomers() {
        Map<String, Object> result = simpleJdbcCall.execute();
        return (List<Customer>) result.get("c_cursor");
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerID(rs.getString("customerID"));
        customer.setCustomerUsername(rs.getString("customerUsername"));
        customer.setCustomerEmail(rs.getString("customerEmail"));
        customer.setCustomerAddress(rs.getString("customerAddress"));
        customer.setCustomerContact(rs.getString("customerContact"));
        customer.setCustomerPassword(rs.getString("customerPassword"));
        return customer;
    }
}
