package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();

    Optional<Customer> selectCustomerById(Integer id);

    public void addCustomer(Customer customer);

    public boolean personWithEmailExists(String email);

    boolean deleteCustomerById(Integer id);

    Customer updateCustomer(Customer customer);
}
