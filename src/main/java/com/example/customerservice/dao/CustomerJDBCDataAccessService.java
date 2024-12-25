package com.example.customerservice.dao;

import com.example.customerservice.model.Customer;
import com.example.customerservice.util.CustomerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * The {@code CustumerJDBCDataAccessService} class provides the JDBC-based implementation
 * of the {@link CustomerDao} interface for performing CRUD operations on customer data.
 * It uses {@code JdbcTemplate} from Spring Framework to interact with the database.
 */
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    /**
     * Retrieves a list of all customers from the database.
     *
     * @return a {@code List} of {@code Customer} objects representing all customers.
     */
    @Override
    public List<Customer> selectAllCustomers() {
        String getAllCustomerQuery = "SELECT id, name, email, age FROM customer";
        return jdbcTemplate.query(getAllCustomerQuery, customerRowMapper);
    }

    /**
     * Retrieves a customer from the database by their ID.
     *
     * @param id the ID of the customer to retrieve.
     * @return an {@code Optional} containing the customer if found, or empty if not found.
     */
    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        String getCustomerByIdQuery = "SELECT id, name, email, age  FROM customer WHERE id = ?";
        return jdbcTemplate.query(getCustomerByIdQuery, new Object[]{id}, customerRowMapper).stream().findFirst();
    }

    /**
     * Adds a new customer to the database.
     *
     * @param customer the {@code Customer} object containing the details of the customer to add.
     */
    @Override
    public void addCustomer(Customer customer) {
        String addCustomerQuery = "INSERT INTO customer (name, email, age) VALUES (?, ?, ?)";
        jdbcTemplate.update(addCustomerQuery, customer.getName(), customer.getEmail(), customer.getAge());
    }

    /**
     * Checks if a customer with the specified email exists in the database.
     *
     * @param email the email address to check.
     * @return {@code true} if a customer with the email exists, otherwise {@code false}.
     */
    @Override
    public boolean personWithEmailExists(String email) {
        String emailExistsQuery = "SELECT EXISTS(SELECT 1 FROM customer WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(emailExistsQuery, Boolean.class, email));
    }

    /**
     * Deletes a customer from the database by their ID.
     *
     * @param id the ID of the customer to delete.
     * @return {@code true} if the customer was successfully deleted, otherwise {@code false}.
     */
    @Override
    public boolean deleteCustomerById(Integer id) {
        String deleteCustomerQuery = "DELETE FROM customer WHERE id = ?";
        return jdbcTemplate.update(deleteCustomerQuery, id) > 0;
    }

    /**
     * Updates the details of an existing customer in the database.
     *
     * @param customer the {@code Customer} object containing the updated details.
     * @return the updated {@code Customer} object.
     */
    @Override
    public Customer updateCustomer(Customer customer) {
        String customerUpdateQuery = "UPDATE customer SET name = ?, email = ?, age = ? WHERE id = ?";
        jdbcTemplate.update(customerUpdateQuery, customer.getName(), customer.getEmail(), customer.getAge(), customer.getId());
        return customer;
    }
}
