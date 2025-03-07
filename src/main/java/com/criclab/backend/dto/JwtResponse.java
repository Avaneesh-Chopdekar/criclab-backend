package com.criclab.backend.dto;

public record JwtResponse(
    String accessToken,
    String refreshToken,
    String email
) {
}
