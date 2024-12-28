package com.example.customerservice.dao;

import com.example.customerservice.AbstractTestContainer;
import com.example.customerservice.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;


    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void testExistsByEmail() {
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                getFaker().name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        //When
        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsByEmail(email);

        //Then
        assertThat(actual).isTrue();
    }

    @Test
    void testExistsByEmailisNull() {
        String email = getFaker().internet().emailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(
                getFaker().name().fullName(),
                email,
                20
        );
        underTest.save(customer);

        // extract the email without the uuid part
        var emailStart = email.substring(0, email.indexOf("-"));
        //get the email from @ symbol till the end
        var emailEnd = email.substring(email.indexOf("@"));
        //Combine emailStar and emailEnd
        var emailNew = emailStart + emailEnd;
        //When
        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean actual = underTest.existsByEmail(emailNew);

        //Then
        assertThat(actual).isFalse();
    }

}