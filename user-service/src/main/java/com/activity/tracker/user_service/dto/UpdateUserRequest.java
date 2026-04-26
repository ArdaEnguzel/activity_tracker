package com.activity.tracker.user_service.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        @Email String email,
        String firstName,
        String lastName,
        String phoneNumber,
        Boolean active
) {}
