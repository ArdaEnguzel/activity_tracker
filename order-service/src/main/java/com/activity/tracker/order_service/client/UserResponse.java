package com.activity.tracker.order_service.client;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Boolean active;
}