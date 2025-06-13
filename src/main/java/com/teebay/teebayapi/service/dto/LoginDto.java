package com.teebay.teebayapi.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
