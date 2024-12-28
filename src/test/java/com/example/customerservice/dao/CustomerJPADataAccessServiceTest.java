package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.MockitoAnnotations.openMocks;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;


    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository repository;


    @BeforeEach
    void setUp() {
        autoCloseable = openMocks(this);
        underTest = new CustomerJPADataAccessService(repository);
    }

    @AfterEach
    void tearDown() {
        try {
            autoCloseable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        // Then
        Mockito.verify(repository).findAll();
    }

    @Test
    void selectCustomerById() {
        //when
        underTest.selectCustomerById(1);
        //Then
        Mockito.verify(repository).findById(1);
    }

    @Test
    void addCustomer() {
//        Faker faker = new Faker();
//        Customer customer = new Customer(
//                faker.name().fullName(),
//                faker.internet().emailAddress() + "-" + UUID.randomUUID(),
//                20
//        );
        //when
        underTest.addCustomer(null);
        //Then
        Mockito.verify(repository).save(null);
    }

    @Test
    void personWithEmailExists() {
        //when
        String email = "manojoshi1982@gmail.com";
        underTest.personWithEmailExists(email);
        //Then
        Mockito.verify(repository).existsByEmail(email);
    }

    @Test
    void deleteCustomerById() {
        //when
        underTest.deleteCustomerById(1);
        //Then
        Mockito.verify(repository).deleteById(1);
    }

    @Test
    void updateCustomer() {
        //Given
        Faker faker = new Faker();
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().emailAddress() + "-" + UUID.randomUUID(),
                20
        );
        //when
        underTest.updateCustomer(customer);
        //Then
        Mockito.verify(repository).save(customer);
    }
}