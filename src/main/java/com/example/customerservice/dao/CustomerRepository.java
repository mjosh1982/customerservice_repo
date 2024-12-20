package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);
}
