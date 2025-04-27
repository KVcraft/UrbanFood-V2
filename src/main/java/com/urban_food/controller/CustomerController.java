package com.urban_food.controller;

import com.urban_food.entity.Customer;
import com.urban_food.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    @PostMapping
    public void addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
    }

    @PutMapping
    public void updateCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") String id) {
        customerService.deleteCustomer(id);
    }
    @GetMapping("/search")
    public List<Customer> searchCustomers(@RequestParam("keyword") String keyword) {
        return customerService.searchCustomers(keyword);
    }
    @PostMapping("/login")
    public boolean login(@RequestBody Customer customer) {
        return customerService.login(customer.getCustomerUsername(), customer.getCustomerPassword());
    }




}
