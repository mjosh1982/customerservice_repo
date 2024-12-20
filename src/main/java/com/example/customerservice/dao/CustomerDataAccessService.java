package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerDataAccessService implements CustomerDao {
    private static final List<Customer> customers;


    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21
        );
        customers.add(alex);

        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                19
        );
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean personWithEmailExists(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean deleteCustomerById(Integer id) {
        Optional<Customer> customerMaybe = selectCustomerById(id);
        if (!customerMaybe.isEmpty()) {
            customers.remove(customerMaybe.get());
            return true;
        }
        return false;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Optional<Customer> customerMaybe = selectCustomerById(customer.getId());
        if(!customerMaybe.isEmpty()) {
            customers.set(customers.indexOf(customerMaybe.get()), customer);
            return customer;
        } else {
            return null;
        }
    }
}
