package com.criclab.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAndJwtResponse {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private boolean isSuperAdmin;

    @NotBlank
    private boolean isEnabled;
}
