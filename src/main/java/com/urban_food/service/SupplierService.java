package com.urban_food.service;

import com.urban_food.entity.Supplier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class SupplierService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall createSupplierCall;
    private SimpleJdbcCall getAllSuppliersCall;
    private SimpleJdbcCall getSupplierByIdCall;
    private SimpleJdbcCall updateSupplierCall;
    private SimpleJdbcCall deleteSupplierCall;

    @PostConstruct
    public void init() {
        createSupplierCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("CREATE_SUPPLIER_PROC")
                .declareParameters(
                        new SqlParameter("p_supplier_id", Types.VARCHAR),
                        new SqlParameter("p_name", Types.VARCHAR),
                        new SqlParameter("p_contact", Types.VARCHAR),
                        new SqlParameter("p_email", Types.VARCHAR),
                        new SqlParameter("p_address", Types.VARCHAR)
                );

        getAllSuppliersCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_ALL_SUPPLIERS_PROC")
                .returningResultSet("o_cursor", (rs, rowNum) -> mapSupplier(rs));

        getSupplierByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_SUPPLIER_BY_ID_PROC")
                .declareParameters(new SqlParameter("p_supplier_id", Types.VARCHAR))
                .returningResultSet("o_cursor", (rs, rowNum) -> mapSupplier(rs));

        updateSupplierCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UPDATE_SUPPLIER_PROC");

        deleteSupplierCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DELETE_SUPPLIER_PROC");
    }


    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(rs.getString("supplierId"));
        supplier.setSupplierName(rs.getString("supplierName"));
        supplier.setSupplierContact(rs.getString("supplierContact"));
        supplier.setSupplierEmail(rs.getString("supplierEmail"));
        supplier.setSupplierAddress(rs.getString("supplierAddress"));
        return supplier;
    }

    public void createSupplier(Supplier supplier) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_supplier_id", supplier.getSupplierId()); // ADD THIS LINE to manually pass ID
        params.put("p_name", supplier.getSupplierName());
        params.put("p_contact", supplier.getSupplierContact());
        params.put("p_email", supplier.getSupplierEmail());
        params.put("p_address", supplier.getSupplierAddress());
        createSupplierCall.execute(params);
    }


    public List<Supplier> getAllSuppliers() {
        Map<String, Object> result = getAllSuppliersCall.execute(new HashMap<>());
        return (List<Supplier>) result.get("o_cursor");
    }

    public Optional<Supplier> getSupplierById(String id) {
        Map<String, Object> result = getSupplierByIdCall.execute(Collections.singletonMap("p_supplier_id", id));
        List<Supplier> list = (List<Supplier>) result.get("o_cursor");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updateSupplier(Supplier supplier) {
        // Check if the supplier ID exists in the database
        Optional<Supplier> existingSupplier = getSupplierById(supplier.getSupplierId());
        if (!existingSupplier.isPresent()) {
            throw new RuntimeException("Supplier ID does not exist. Update failed.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("p_supplier_id", supplier.getSupplierId());  // Ensure primary key cannot be updated
        params.put("p_name", supplier.getSupplierName());
        params.put("p_contact", supplier.getSupplierContact());
        params.put("p_email", supplier.getSupplierEmail());
        params.put("p_address", supplier.getSupplierAddress());
        updateSupplierCall.execute(params);
    }

    public void deleteSupplier(String id) {
        // Check if the supplier ID exists in the database
        Optional<Supplier> existingSupplier = getSupplierById(id);
        if (!existingSupplier.isPresent()) {
            throw new RuntimeException("Supplier ID does not exist. Deletion failed.");
        }

        deleteSupplierCall.execute(Collections.singletonMap("p_supplier_id", id));
    }
}
