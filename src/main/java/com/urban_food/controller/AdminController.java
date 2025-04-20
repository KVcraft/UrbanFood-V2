package com.urban_food.controller;

import com.urban_food.entity.Admin;
import com.urban_food.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping
    public String createAdmin(@RequestBody Admin admin) {
        adminService.createAdmin(admin);
        return "Admin created successfully!";
    }

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    @GetMapping("/{id}")
    public Optional<Admin> getAdminById(@PathVariable String id) {
        return adminService.getAdminById(id);
    }

    @PutMapping
    public String updateAdmin(@RequestBody Admin admin) {
        adminService.updateAdmin(admin);
        return "Admin updated successfully!";
    }

    @DeleteMapping("/{id}")
    public String deleteAdmin(@PathVariable String id) {
        adminService.deleteAdmin(id);
        return "Admin deleted successfully!";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody Admin admin) {
        String result = adminService.loginAdmin(admin.getAdminUsername(), admin.getAdminPassword());
        return ResponseEntity.ok(result); // Return just the result string
    }
}
