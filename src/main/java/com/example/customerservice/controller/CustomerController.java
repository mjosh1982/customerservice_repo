package com.example.customerservice.controller;

import com.example.customerservice.model.Customer;
import com.example.customerservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable("id") Integer id) {
        return getById(id);
    }

    public Customer getById(Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("/add")
    public void addCustomer(@RequestBody CustomerRegistrationRequest request) {
        customerService.addCustomer(request);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteCustomerWithId(@PathVariable("id") Integer id) {
        return customerService.deleteCustomerById(id);
    }

    @PutMapping("/update/{id}")
    public Customer updateCustomer(@PathVariable("id") Integer id, @RequestBody CustomerRegistrationRequest request) {
        Customer customer = getById(id);
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());
        return customerService.updateCustomerDetails(customer);
    }
}
