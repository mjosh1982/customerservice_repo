package com.example.customerservice.controller;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
