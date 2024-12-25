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


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

    private CustomerJDBCDataAccessService underTest;
    private JdbcTemplate jdbcTemplate = getJdbcTemplate();
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(jdbcTemplate, customerRowMapper);
    }

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

    @Test
    void willReturnEmptyCustomerIdIsNullForSelectCustomerById() {
        int id = -1;
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isEmpty();
    }

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

    @Test
    void deleteCustomerByIdDoesNotExist() {
        // Given
        int id = -1;

        // When
        boolean result = underTest.deleteCustomerById(id);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void personWithEmailDoesNotExist() {
        // Given
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();

        // When
        boolean exists = underTest.personWithEmailExists(email);

        // Then
        assertThat(exists).isFalse();
    }

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