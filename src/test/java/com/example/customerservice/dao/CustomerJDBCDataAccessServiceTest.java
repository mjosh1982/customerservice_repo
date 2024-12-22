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

    private CustumerJDBCDataAccessService underTest;
    private JdbcTemplate jdbcTemplate = getJdbcTemplate();
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustumerJDBCDataAccessService(jdbcTemplate, customerRowMapper);
    }

    @Test
    void addCustomer() {

        //Given

        //When

        //Then
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
    void personWithEmailExists() {

        //Given

        //When

        //Then
    }

    @Test
    void deleteCustomerById() {

        //Given

        //When

        //Then
    }

    @Test
    void updateCustomer() {

        //Given

        //When

        //Then
    }
}