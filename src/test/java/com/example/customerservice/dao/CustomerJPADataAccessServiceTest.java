package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * Unit test class for CustomerJPADataAccessService.
 * This class verifies the behavior of various methods
 * that interact with the CustomerRepository.
 */
class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;


    // Used to automatically close resources initialized by openMocks() after each test.
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


    /**
     * Tests that selectAllCustomers() method invokes findAll() on the repository.
     */
    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        // Then
        Mockito.verify(repository).findAll();
    }

    /**
     * Verifies that selectCustomerById() method calls findById() with the correct ID.
     */
    @Test
    void selectCustomerById() {
        //when
        underTest.selectCustomerById(1);
        //Then
        Mockito.verify(repository).findById(1);
    }

    /**
     * Ensures that the addCustomer() method triggers save() on the repository with the provided Customer object.
     */
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

    /**
     * Validates that personWithEmailExists() invokes existsByEmail() on the repository with a given email.
     */
    @Test
    void personWithEmailExists() {
        //when
        String email = "manojoshi1982@gmail.com";
        underTest.personWithEmailExists(email);
        //Then
        Mockito.verify(repository).existsByEmail(email);
    }

    /**
     * Confirms that deleteCustomerById() performs deleteById() after verifying the existence of the ID.
     */
    @Test
    void deleteCustomerById() {

        when(repository.existsById(1)).thenReturn(true);

        //when
        underTest.deleteCustomerById(1);

        //Then
        Mockito.verify(repository).deleteById(1);
    }

    /**
     * Verifies that updateCustomer() updates the customer record by calling save() on the repository.
     */
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