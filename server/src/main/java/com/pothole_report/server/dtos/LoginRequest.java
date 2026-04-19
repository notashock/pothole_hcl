package com.pothole_report.server.dtos;

import com.pothole_report.server.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email is required")
    @Email(message = "Invalid Email Format")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6, message = "password must be minimum 6 characters long")
    private String password;
    private Role role;
}
