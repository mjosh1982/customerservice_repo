package com.example.customerservice.util;

import com.example.customerservice.model.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CustomerRowMapperTest {

    private Faker faker;
    private CustomerRowMapper customerRowMapper;


    @BeforeEach
    void setUp() {
        faker = new Faker();
        customerRowMapper = new CustomerRowMapper();
    }

    @Test
    void mapRow() throws SQLException {
        //create a list of customers using javafaker
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customers.add(new Customer(
                    faker.number().randomDigit(),
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.number().numberBetween(18, 80)
            ));
        }
        //create a resultset with the list of customers
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(customers.get(0).getId());
        when(resultSet.getString("name")).thenReturn(customers.get(0).getName());
        when(resultSet.getString("email")).thenReturn(customers.get(0).getEmail());
        when(resultSet.getInt("age")).thenReturn(customers.get(0).getAge());

        //create a customerRowMapper object
        //call the mapRow method
        Customer customer = customerRowMapper.mapRow(resultSet, 1);
        //assert that the customer object is equal to the first customer in the list
        assertEquals(customers.get(0).getEmail(), customer.getEmail());
        assertEquals(customers.get(0).getName(), customer.getName());
        assertEquals(customers.get(0).getAge(), customer.getAge());
        assertEquals(customers.get(0).getId(), customer.getId());
    }
}