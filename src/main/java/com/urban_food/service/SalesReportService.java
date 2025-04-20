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

    public void generateSalesReport(SalesReport report) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_salesReportId", report.getSalesReportId());
        params.put("p_startDate", new Date(report.getStartDate().getTime()));
        params.put("p_endDate", new Date(report.getEndDate().getTime()));

        generateSalesReportCall.execute(params);
    }
}
