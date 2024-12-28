package com.example.customerservice.dao;

import com.example.customerservice.AbstractTestContainer;
import com.example.customerservice.model.Customer;
import com.example.customerservice.util.CustomerRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * Test suite for CustomerJDBCDataAccessService.
 * This class contains unit tests to validate the core functionalities of CustomerJDBCDataAccessService,
 * such as adding, updating, deleting, and fetching customers from the database.
 */
class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private JdbcTemplate jdbcTemplate = getJdbcTemplate();
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    /**
     * Sets up the test environment before each test execution.
     * Initializes the instance of CustomerJDBCDataAccessService with required dependencies.
     */
    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(jdbcTemplate, customerRowMapper);
    }

    /**
     * Tests adding a customer to the database.
     * Verifies that the customer is successfully added and can be retrieved from the database.
     */
    @Test
    void addCustomer() {

        //Given
        Customer customer = new Customer(
                getFaker().name().fullName(),
                getFaker().internet().emailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.addCustomer(customer);

        //When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();
    }

    /**
     * Tests retrieving all customers from the database.
     * Verifies that the method retrieves a non-empty list after adding a customer.
     */
    @Test
    void selectAllCustomers() {

        //Given
        Customer customer = new Customer(
                getFaker().name().fullName(),
                getFaker().internet().emailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.addCustomer(customer);

        //When
        List<Customer> customers = underTest.selectAllCustomers();

        //Then
        assertThat(customers).isNotEmpty();
    }

    /**
     * Tests retrieving a specific customer by their ID.
     * Ensures that a customer can be correctly fetched by their ID, and all their details match the expected values.
     */
    @Test
    void selectCustomerById() {

        //Given
        Customer customer = new Customer(
                getFaker().name().fullName(),
                getFaker().internet().emailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.addCustomer(customer);

        //When
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                    assertThat(c.getName()).isEqualTo(customer.getName());

                }
        );

    }

    /**
     * Tests the scenario where a customer is fetched by an invalid or non-existing ID.
     * Verifies that the method returns an empty Optional.
     */
    @Test
    void willReturnEmptyCustomerIdIsNullForSelectCustomerById() {
        int id = -1;
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();
    }

    /**
     * Tests checking if a customer with a specific email exists in the database.
     * Verifies that the method correctly returns true when the email exists.
     */
    @Test
    void personWithEmailExists() {

        // Given
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                getFaker().name().fullName(),
                email,
                20
        );
        underTest.addCustomer(customer);

        // When
        boolean exists = underTest.personWithEmailExists(email);

        // Then
        assertThat(exists).isTrue();
    }

    /**
     * Tests deleting a customer by ID.
     * Ensures that the customer is successfully deleted and cannot be retrieved afterward.
     */
    @Test
    void deleteCustomerById() {

        //Given
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                getFaker().name().fullName(),
                email,
                20
        );
        underTest.addCustomer(customer);

        //When
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        underTest.deleteCustomerById(id);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();
    }

    /**
     * Tests the scenario where a deletion is attempted for a non-existing customer ID.
     * Verifies that the method correctly returns false.
     */
    @Test
    void deleteCustomerByIdDoesNotExist() {
        // Given
        int id = -1;

        // When
        boolean result = underTest.deleteCustomerById(id);

        // Then
        assertThat(result).isFalse();
    }

    /**
     * Tests checking if a customer with a specific email exists when it does not.
     * Verifies that the method correctly returns false.
     */
    @Test
    void personWithEmailDoesNotExist() {
        // Given
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();

        // When
        boolean exists = underTest.personWithEmailExists(email);

        // Then
        assertThat(exists).isFalse();
    }

    /**
     * Tests updating a customer's details in the database.
     * Ensures that the updated details are correctly saved and retrieved.
     */
    @Test
    void updateCustomer() {

        //Given
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                getFaker().name().fullName(),
                email,
                20
        );
        underTest.addCustomer(customer);

        //When
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //Update the name to Manoj Joshi and age to 50 for the customer with the given id
        Customer updatedCustomer = new Customer(
                id,
                "Manoj Joshi",
                email,
                50
        );
        underTest.updateCustomer(updatedCustomer);

        //Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(
                c -> {
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getEmail()).isEqualTo(email);
                    assertThat(c.getAge()).isEqualTo(50);
                    assertThat(c.getName()).isEqualTo("Manoj Joshi");
                }
        );
    }
}