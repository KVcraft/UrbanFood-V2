package com.urban_food.controller;

import com.urban_food.entity.SalesReport;
import com.urban_food.service.SalesReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/salesreport")
public class SalesReportController {
    @Autowired
    private SalesReportService salesReportService;

    @PostMapping("/generate")
    public ResponseEntity<SalesReport> generateReport(@RequestBody SalesReport report) {
        SalesReport generated = salesReportService.generateSalesReport(report);
        return ResponseEntity.ok(generated);
    }
}
