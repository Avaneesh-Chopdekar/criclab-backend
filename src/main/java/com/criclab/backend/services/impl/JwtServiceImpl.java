package com.criclab.backend.services.impl;

import com.criclab.backend.config.CustomProperties;
import com.criclab.backend.services.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final CustomProperties customProperties;

    public String generateToken(String username, boolean isAccessToken) {
        long expiration = isAccessToken ? customProperties.getAccessTokenExpiration() : customProperties.getRefreshTokenExpiration();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new java.util.Date(System.currentTimeMillis()))
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(customProperties.getJwtSecret().getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(customProperties.getJwtSecret().getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(customProperties.getJwtSecret().getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
