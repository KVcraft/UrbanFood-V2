package com.urban_food.service;

import com.urban_food.entity.SalesReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SalesReportService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall generateSalesReportCall;

    @Autowired
    public SalesReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.generateSalesReportCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GENERATE_SALES_REPORT_PROC");
    }

    public SalesReport generateSalesReport(SalesReport report) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_startDate", new Date(report.getStartDate().getTime()));
        params.put("p_endDate", new Date(report.getEndDate().getTime()));

        generateSalesReportCall.execute(params);

        // Fetch latest inserted report
        String sql = """
        SELECT * FROM SalesReport 
        WHERE startDate = ? AND endDate = ?
        ORDER BY generatedAt DESC 
        FETCH FIRST 1 ROWS ONLY
    """;

        return jdbcTemplate.queryForObject(sql, new Object[] {
                new Date(report.getStartDate().getTime()),
                new Date(report.getEndDate().getTime())
        }, (rs, rowNum) -> {
            SalesReport sr = new SalesReport();
            sr.setSalesReportId(rs.getString("salesReportId"));
            sr.setStartDate(rs.getDate("startDate"));
            sr.setEndDate(rs.getDate("endDate"));
            sr.setTotalOrders(rs.getInt("totalOrders"));
            sr.setTotalSalesAmount(rs.getDouble("totalSalesAmount"));
            sr.setGeneratedAt(rs.getTimestamp("generatedAt"));
            return sr;
        });
    }
}
