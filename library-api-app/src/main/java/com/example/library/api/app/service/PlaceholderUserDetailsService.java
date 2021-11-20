package com.example.library.api.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PlaceholderUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceholderUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("User details requested for {}",username);
        if(username.equals("user")) {
            return new User(username,"dXNlcjAxMQ==",
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        } else if(username.equals("admin")) {
            return new User(username,"YWRtaW4wMTE=",
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
        } else {
            throw new UsernameNotFoundException(String.format("User %s not found",username));
        }
    }
}
