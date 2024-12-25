package com.example.customerservice.model;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
