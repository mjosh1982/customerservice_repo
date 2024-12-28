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

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }


    @Test
    void getAllCustomers() {
        //when
        underTest.getAllCustomers();

        //then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomerById() {


        int id = 10;
        Customer customer = new Customer(id, "Manoj", "manojoshi1982@gmail.com", 42);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //when
        Customer actual = underTest.getCustomerById(10);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomerByIdReturnsNull() {
        int id = 10;
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());


        //then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessageContaining("Customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {

        //Given
        String email = "manojoshi1982@gmail.com";

        when(customerDao.personWithEmailExists(email)).thenReturn(false);

        //When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Manoj", email, 42);

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).addCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }


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


    @Test
    void deleteCustomerById() {
    }

    @Test
    void updateCustomerDetails() {
    }
}