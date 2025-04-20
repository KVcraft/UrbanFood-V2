package com.urban_food.controller;

import com.urban_food.entity.SalesReport;
import com.urban_food.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/salesreport")
public class SalesReportController {
    @Autowired
    private SalesReportService salesReportService;

    @PostMapping("/generate")
    public String generateSalesReport(@RequestBody SalesReport report) {
        // Use the service to generate the report
        salesReportService.generateSalesReport(report);
        return "Sales Report generated successfully!";

    }
}
