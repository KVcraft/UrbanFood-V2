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
                        new SqlParameter("p_productId", Types.VARCHAR),
                        new SqlParameter("p_productName", Types.VARCHAR),
                        new SqlParameter("p_productDescription", Types.VARCHAR),
                        new SqlParameter("p_productPrice", Types.NUMERIC),
                        new SqlParameter("p_stockQuantity", Types.INTEGER),
                        new SqlParameter("p_category", Types.VARCHAR)
                );

        getAllProductsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_ALL_PRODUCTS_PROC")
                .returningResultSet("o_cursor", (rs, rowNum) -> mapProduct(rs));

        getProductByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_PRODUCT_BY_ID_PROC")
                .declareParameters(new SqlParameter("p_productId", Types.VARCHAR))
                .returningResultSet("o_cursor", (rs, rowNum) -> mapProduct(rs));

        updateProductCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("UPDATE_PRODUCT_PROC")
                .declareParameters(
                        new SqlParameter("p_productId", Types.VARCHAR),
                        new SqlParameter("p_productName", Types.VARCHAR),
                        new SqlParameter("p_productDescription", Types.VARCHAR),
                        new SqlParameter("p_productPrice", Types.NUMERIC),
                        new SqlParameter("p_stockQuantity", Types.INTEGER),
                        new SqlParameter("p_category", Types.VARCHAR)
                );

        deleteProductCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DELETE_PRODUCT_PROC")
                .declareParameters(new SqlParameter("p_productId", Types.VARCHAR));
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getString("productId"));
        product.setProductName(rs.getString("productName"));
        product.setProductDescription(rs.getString("productDescription"));
        product.setProductPrice(rs.getBigDecimal("productPrice"));
        product.setStockQuantity(rs.getInt("stockQuantity"));
        product.setCategory(rs.getString("category"));
        return product;
    }

    public void createProduct(Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_productId", product.getProductId()); // now required
        params.put("p_productName", product.getProductName());
        params.put("p_productDescription", product.getProductDescription());
        params.put("p_productPrice", product.getProductPrice());
        params.put("p_stockQuantity", product.getStockQuantity());
        params.put("p_category", product.getCategory());
        createProductCall.execute(params);
    }

    public List<Product> getAllProducts() {
        Map<String, Object> result = getAllProductsCall.execute(new HashMap<>());
        return (List<Product>) result.get("o_cursor");
    }

    public Optional<Product> getProductById(String productId) {
        Map<String, Object> result = getProductByIdCall.execute(Collections.singletonMap("p_productId", productId));
        List<Product> list = (List<Product>) result.get("o_cursor");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updateProduct(Product product) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_productId", product.getProductId());
        params.put("p_productName", product.getProductName());
        params.put("p_productDescription", product.getProductDescription());
        params.put("p_productPrice", product.getProductPrice());
        params.put("p_stockQuantity", product.getStockQuantity());
        params.put("p_category", product.getCategory());
        updateProductCall.execute(params);
    }

    public void deleteProduct(String productId) {
        deleteProductCall.execute(Collections.singletonMap("p_productId", productId));
    }
}
