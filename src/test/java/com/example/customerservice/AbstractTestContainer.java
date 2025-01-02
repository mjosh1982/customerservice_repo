package com.example.customerservice;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

/**
 * AbstractTestContainer sets up a reusable Testcontainers PostgreSQL container 
 * for integration tests and provides utility methods for database and data handling.
 */
@Testcontainers
public abstract class AbstractTestContainer {

    public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";

    /**
     * Executes database migrations using Flyway on the PostgreSQL test container 
     * before all tests are executed.
     */
    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword())
                .load();
        flyway.migrate();
        System.out.println("Migrations applied successfully");
    }

    @Container
    protected static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("amigoscode-dao-unit-test")
            .withUsername("amigoscode")
            .withPassword("password");

    /**
     * Registers dynamic properties for the Spring DataSource configuration, 
     * allowing tests to use the database credentials from the test container.
     */
    @DynamicPropertySource
    protected static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    /**
     * Constructs a DataSource instance configured with the PostgreSQL 
     * container's properties for database connections.
     */
    protected static DataSource getDataSource() {
        return DataSourceBuilder.create().driverClassName(ORG_POSTGRESQL_DRIVER)
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword()).build();
    }

    /**
     * Provides a JdbcTemplate instance based on the container-configured DataSource, 
     * simplifying SQL operations within tests.
     */
    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    /**
     * Creates and returns a Faker instance, enabling the generation of random 
     * test data such as names, addresses, and more.
     */
    protected static Faker getFaker() {
        return new Faker();
    }

}
