package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDao {

    private final CustomerRepository customerRepository;

    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public boolean personWithEmailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean deleteCustomerById(Integer id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
//        Optional<Customer> customerFromDb = customerRepository.findById(customer.getId());
//        if (!customerFromDb.isEmpty()) {
//            Customer customerToUpdate = customerFromDb.get();
//            customerToUpdate.setName(customer.getName());
//            customerToUpdate.setEmail(customer.getEmail());
//            customerToUpdate.setAge(customer.getAge());
        return customerRepository.save(customer);
//        } else {
//            return null;
//        }
    }
}
