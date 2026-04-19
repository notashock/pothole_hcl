package com.pothole_report.server.dtos;

import com.pothole_report.server.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RegisterRequest {
    private String name;
    @NotBlank(message = "email required")
    @Email(message = "Not a valid email")
    private String email;
    @NotBlank(message = "password Required")
    @Size(min = 6, message = "password should be min of 6 characters long")
    private String password;
    @Pattern(regexp = "^[0-9]{10}$", message = "invalid mobile number")
    private String phn_num;
    private Role role;
}
