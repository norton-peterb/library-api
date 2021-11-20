package com.example.library.api.app.configuration;

import com.example.library.api.app.encoder.Base64PasswordEncoder;
import com.example.library.api.app.service.PlaceholderUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class LibraryApiConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Base64PasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new PlaceholderUserDetailsService();
    }
}
