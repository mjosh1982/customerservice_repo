package com.example.customerservice.service;

import com.example.customerservice.dao.CustomerDao;
import com.example.customerservice.exception.DuplicateResourceFoundException;
import com.example.customerservice.exception.ResourceNotFound;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerRegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test class for CustomerService to ensure business logic and data access methods work as expected.
 * Includes unit tests for all main service methods like add, update, delete, and fetch customers.
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao; // Mocked dependency to simulate DAO layer interactions.
    private CustomerService underTest; // The class under test, instantiated before each test.

    /**
     * Sets up the preconditions for tests by initializing the CustomerService instance
     * and injecting the mocked CustomerDao dependency into it.
     */
    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }


    /**
     * Test to verify that the getAllCustomers method calls
     * the selectAllCustomers method in the CustomerDao layer.
     */
    @Test
    void getAllCustomers() {
        // Act: Call the getAllCustomers method in the service layer.
        underTest.getAllCustomers();
    
        // Assert: Verify that the correct DAO method is invoked.
        verify(customerDao).selectAllCustomers();
    }

    /**
     * Test to verify that getting a customer by their ID returns the correct Customer object.
     * Simulates a successful database query where a customer exists with the given ID.
     */
    @Test
    void getCustomerById() {
    
        // Arrange: Sample customer data and mocking customerDao to return it.
        int id = 10; // ID of the customer to fetch.
        Customer customer = new Customer(id, "Manoj", "manojoshi1982@gmail.com", 42);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
    
        // Act: Call the getCustomerById method on the service.
        Customer actual = underTest.getCustomerById(10);
    
        // Assert: Verify the fetched customer matches the expected one.
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(customer);
    }

    /**
     * Test to verify exception handling when a customer ID is not found in the database.
     * Ensures a ResourceNotFound exception is thrown with the correct message.
     */
    @Test
    void getCustomerByIdReturnsNull() {
        // Arrange: Setup mock for a non-existing customer ID.
        int id = 10; // Non-existent customer ID.
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
    
        // Assert: Verify that the ResourceNotFound exception is thrown.
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer with id [%s] not found".formatted(id));
    }

    /**
     * Test to verify that adding a valid customer works correctly
     * and ensures the data passed to the DAO matches the user input.
     */
    @Test
    void addCustomer() {
    
        // Arrange: Configure mock and create a sample registration request.
        String email = "manojoshi1982@gmail.com"; // New customer email.
        when(customerDao.personWithEmailExists(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Manoj", email, 42);
    
        // Act: Add a new customer using the service method.
        underTest.addCustomer(request);
    
        // Capture the customer passed to the DAO for additional assertions.
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).addCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
    
        // Assert: Validate the captured customer data matches the input.
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }


    /**
     * Test to ensure that attempting to add a customer with a duplicate email address
     * throws a DuplicateResourceFoundException, with the DAO add method not being called.
     */
    @Test
    void addCustomerWithDuplicateEmailError() {

        //Given
        String email = "manojoshi1982@gmail.com";

        when(customerDao.personWithEmailExists(email)).thenReturn(true);

        //When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Manoj", email, 42);

        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceFoundException.class).hasMessageContaining("Email already taken.");

        verify(customerDao, never()).addCustomer(any());

    }


    /**
     * Test to verify that deleting a customer by their ID results in the DAO deleteCustomerById
     * method being called with the correct ID.
     */
    @Test
    void deleteCustomerById() {
        //when
        underTest.deleteCustomerById(10);
        //then
        verify(customerDao).deleteCustomerById(10);
    }

    /**
     * Test to ensure that updating a customer's details works correctly by verifying
     * that the correct customer data is provided to the DAO update method and matches the expected data.
     */
    @Test
    void updateCustomerDetails() {
        //when
        Customer customer = new Customer(10, "Manoj", "manojoshi1982@gmail.com", 42);

        when(customerDao.updateCustomer(any())).thenReturn(customer);

        Customer customer1 = underTest.updateCustomerDetails(customer);

        //then

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer actual = customerArgumentCaptor.getValue();

        assertThat(actual).isEqualTo(customer);
    }
}