package com.example.library.api.app.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;

public class Base64PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return Base64.getEncoder().encodeToString(
                rawPassword.toString().getBytes()
        );
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encodedMatch = Base64.getEncoder().encodeToString(
                rawPassword.toString().getBytes()
        );
        return encodedMatch.equals(encodedPassword);
    }
}
