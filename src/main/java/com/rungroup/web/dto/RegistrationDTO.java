package com.rungroup.web.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private Long id;

    @NotBlank(message = "Username cannot be empty")
    private String userName;

    @NotBlank(message = "Email cannot be empty")
    private String email;


    @NotBlank(message = "Password cannot be empty")
    private String password;
}
