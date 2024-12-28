package com.example.customerservice.service;

import com.example.customerservice.dao.CustomerDao;
import com.example.customerservice.exception.DuplicateResourceFoundException;
import com.example.customerservice.exception.ResourceNotFound;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerRegistrationRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {


    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }


    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id) {
        return customerDao.selectCustomerById(id).orElseThrow(() -> new ResourceNotFound("Customer with id [%s] not found".formatted(id)));
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
        return customerDao.personWithEmailExists(email);
    }

    public boolean deleteCustomerById(Integer id) {
        return customerDao.deleteCustomerById(id);
    }

    public Customer updateCustomerDetails(Customer customer) {
        return customerDao.updateCustomer(customer);
    }
}
