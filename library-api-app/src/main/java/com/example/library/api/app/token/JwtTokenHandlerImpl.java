package com.example.library.api.app.token;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtTokenHandlerImpl implements JwtTokenHandler {

    private static final long TOKEN_PERIOD = 7200000L;

    private final String secret;

    public JwtTokenHandlerImpl(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String getTokenUsername(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        return claims.getBody().getExpiration();
    }

    public String generateNewToken(UserDetails userDetails) {
        return Jwts.builder().setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_PERIOD ))
                .signWith(SignatureAlgorithm.HS512,secret).compact();
    }

    public boolean validateToken(String token,UserDetails userDetails) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        String username = claims.getBody().getSubject();
        Date expiration = claims.getBody().getExpiration();
        return (username.equals(userDetails.getUsername()))
                && expiration.after(new Date());
    }
}
