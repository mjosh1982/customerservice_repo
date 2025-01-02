package com.example.customerservice;

import com.example.customerservice.dao.CustomerRepository;
import com.example.customerservice.model.Customer;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CustomerServiceApplication.class, args);
//        String[] beanDefinitionNames = run.getBeanDefinitionNames();
//        Arrays.stream(beanDefinitionNames).forEach(System.out::println);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();

            Customer customer = new Customer(
                    name.fullName(),
                    name.firstName() + "." + name.lastName() + "@gmail.com",
                    random.nextInt(16, 99)
            );

            System.out.println("Saving customers to database...");
            customerRepository.save(customer);
        };
    }
}