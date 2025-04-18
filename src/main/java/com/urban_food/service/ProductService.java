package com.urban_food.service;

import com.urban_food.entity.Product;
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
public class ProductService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall createProductCall;
    private SimpleJdbcCall getAllProductsCall;
    private SimpleJdbcCall getProductByIdCall;
    private SimpleJdbcCall updateProductCall;
    private SimpleJdbcCall deleteProductCall;

    @PostConstruct
    public void init() {
        createProductCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("CREATE_PRODUCT_PROC")
                .declareParameters(
                        new SqlParameter("p_name", Types.VARCHAR),
                        new SqlParameter("p_description", Types.VARCHAR),
                        new SqlParameter("p_price", Types.NUMERIC),
                        new SqlParameter("p_stock_qty", Types.INTEGER),
                        new SqlParameter("p_category", Types.VARCHAR)
                );

        getAllProductsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_ALL_PRODUCTS_PROC")
                .returningResultSet("o_cursor", (rs, rowNum) -> mapProduct(rs));

        getProductByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_PRODUCT_BY_ID_PROC")
                .declareParameters(new SqlParameter("p_product_id", Types.VARCHAR))
                .returningResultSet("o_cursor", (rs, rowNum) -> mapProduct(rs));

        updateProductCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UPDATE_PRODUCT_PROC")
                .declareParameters(
                        new SqlParameter("p_product_id", Types.VARCHAR),
                        new SqlParameter("p_name", Types.VARCHAR),
                        new SqlParameter("p_description", Types.VARCHAR),
                        new SqlParameter("p_price", Types.NUMERIC),
                        new SqlParameter("p_stock_qty", Types.INTEGER),
                        new SqlParameter("p_category", Types.VARCHAR)
                );

        deleteProductCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DELETE_PRODUCT_PROC")
                .declareParameters(new SqlParameter("p_product_id", Types.VARCHAR));
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getString("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStockQty(rs.getInt("stock_qty"));
        product.setCategory(rs.getString("category"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return product;
    }

    public void createProduct(Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_name", product.getName());
        params.put("p_description", product.getDescription());
        params.put("p_price", product.getPrice());
        params.put("p_stock_qty", product.getStockQty());
        params.put("p_category", product.getCategory());
        createProductCall.execute(params);
    }

    public List<Product> getAllProducts() {
        Map<String, Object> result = getAllProductsCall.execute(new HashMap<>());
        return (List<Product>) result.get("o_cursor");
    }

    public Optional<Product> getProductById(String productId) {
        Map<String, Object> result = getProductByIdCall.execute(Collections.singletonMap("p_product_id", productId));
        List<Product> list = (List<Product>) result.get("o_cursor");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updateProduct(Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_product_id", product.getProductId());
        params.put("p_name", product.getName());
        params.put("p_description", product.getDescription());
        params.put("p_price", product.getPrice());
        params.put("p_stock_qty", product.getStockQty());
        params.put("p_category", product.getCategory());
        updateProductCall.execute(params);
    }

    public void deleteProduct(String productId) {
        deleteProductCall.execute(Collections.singletonMap("p_product_id", productId));
    }
}
