package com.urban_food.service;

import com.urban_food.entity.Customer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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

    @SuppressWarnings("unchecked")
    public List<Customer> getAllCustomers() {
        Map<String, Object> result = simpleJdbcCall.execute();
        System.out.println("Stored Procedure Output: " + result);
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
    public void addCustomer(Customer customer) {
        jdbcTemplate.update("CALL customer_pkg.add_customer(?, ?, ?, ?, ?, ?)",
                customer.getCustomerID(),
                customer.getCustomerUsername(),
                customer.getCustomerEmail(),
                customer.getCustomerAddress(),
                customer.getCustomerContact(),
                customer.getCustomerPassword());
    }

    public void updateCustomer(Customer customer) {
        jdbcTemplate.update("CALL customer_pkg.update_customer(?, ?, ?, ?, ?, ?)",
                customer.getCustomerID(),
                customer.getCustomerUsername(),
                customer.getCustomerEmail(),
                customer.getCustomerAddress(),
                customer.getCustomerContact(),
                customer.getCustomerPassword());
    }

    public void deleteCustomer(String customerID) {
        jdbcTemplate.update("CALL customer_pkg.delete_customer(?)", customerID);
    }
    public List<Customer> searchCustomers(String keyword) {
        SimpleJdbcCall searchCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("customer_pkg")
                .withProcedureName("search_customers")
                .declareParameters(
                        new SqlParameter("p_keyword", Types.VARCHAR),
                        new SqlReturnResultSet("c_cursor", (rs, rowNum) -> mapCustomer(rs))
                );

        Map<String, Object> result = searchCall.execute(Map.of("p_keyword", keyword));
        return (List<Customer>) result.get("c_cursor");
    }

}
