package com.example.customerservice;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
public class TestContainersTest extends AbstractTestContainer {


    @Test
    void canStartPostgresDB() {
        //Given
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
//        assertThat(postgreSQLContainer.isHealthy()).isTrue();

        //When

        //Then
    }
}
