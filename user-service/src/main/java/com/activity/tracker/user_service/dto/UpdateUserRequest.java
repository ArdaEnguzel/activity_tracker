package com.activity.tracker.user_service.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Email
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean active;
}