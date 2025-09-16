package org.kshitij.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class LoginRequestDto {
    @NotEmpty(message = "Email cannot be blank ")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is required")
    @Size(min= 6, message = "Password must be 6 characters long")
    private String password;


}
