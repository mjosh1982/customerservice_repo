package com.example.customerservice.service;

import com.example.customerservice.controller.CustomerRegistrationRequest;
import com.example.customerservice.dao.CustomerDao;
import com.example.customerservice.exception.DuplicateResourceFoundException;
import com.example.customerservice.exception.ResourceNotFound;
import com.example.customerservice.model.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {


    private final CustomerDao customerDao;
    private final JdbcTemplate jdbcTemplate;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, JdbcTemplate jdbcTemplate) {
        this.customerDao = customerDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> getAllCustomers() {
        var getAllCustomers = "select id, name, email, age from customer";
        RowMapper<Customer> customerRowMapper = (rs, rowNum) -> new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getInt("age")
        );
        List<Customer> customers = jdbcTemplate.query(getAllCustomers, customerRowMapper);
        return customers;
    }

    public Customer getCustomerById(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound("Customer with id [%s] not found.".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest request) {
        String email = request.email();

        if (personWithEmailExists(email)) {
            throw new DuplicateResourceFoundException("Email already taken.");
        }

        //Convert the request to customer object
        Customer customer = new Customer(
                request.name(),
                email,
                request.age()
        );
        customerDao.addCustomer(customer);
    }

    private boolean personWithEmailExists(String email) {
        return getAllCustomers().stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    public boolean deleteCustomerById(Integer id) {
        return customerDao.deleteCustomerById(id);
    }

    public Customer updateCustomerDetails(Customer customer) {
        return customerDao.updateCustomer(customer);
    }
}
