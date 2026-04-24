package com.activity.tracker.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
}