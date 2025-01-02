package com.example.customerservice.journey;

import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String CUSTOMER_URI = "/api/v1/customers";


    @Test
    void registerACustomer() {
        //create a registration request for customer using faker
        Faker faker = new Faker();
        Name name = faker.name();
        String fullName = name.fullName();
        String email = name.lastName() + "." + UUID.randomUUID() + "@gmail.com";
        int age = faker.number().numberBetween(16, 99);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(fullName, email, age);

        //send a post request to the customer controller
        webTestClient.post()
                .uri(CUSTOMER_URI + "/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus().isOk();
        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI + "/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(fullName, email, age);

        assertThat(allCustomers).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").contains(expectedCustomer);

        //get customer by id
        int id = allCustomers.stream().filter(c -> c.getEmail().equals(email)).findFirst().map(Customer::getId).orElseThrow();

        expectedCustomer.setId(id);

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                }).isEqualTo(expectedCustomer);
    }
}
