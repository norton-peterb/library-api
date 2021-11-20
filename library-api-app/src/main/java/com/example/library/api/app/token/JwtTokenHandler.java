package com.example.library.api.app.token;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtTokenHandler {
    String getTokenUsername(String token);
    Date getExpirationDateFromToken(String token);
    String generateNewToken(UserDetails userDetails);
    boolean validateToken(String token,UserDetails userDetails);
}
