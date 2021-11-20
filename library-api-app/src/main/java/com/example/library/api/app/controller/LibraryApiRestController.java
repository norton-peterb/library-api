package com.example.library.api.app.controller;

import com.example.library.api.app.bean.AuthenticationRequest;
import com.example.library.api.app.bean.AuthenticationResponse;
import com.example.library.api.app.token.JwtTokenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryApiRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryApiRestController.class);

    private final JwtTokenHandler jwtTokenHandler;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public LibraryApiRestController(JwtTokenHandler jwtTokenHandler, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.jwtTokenHandler = jwtTokenHandler;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(path = "/test",produces = MediaType.TEXT_HTML_VALUE)
    @Secured("ROLE_ADMIN")
    public String testService() {
        return "<HTML><BODY><H1>Test Page OK</H1><HR></BODY></HTML>";
    }

    @PostMapping(path = "/authenticate",consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        LOGGER.info("Attempting to Authenticate with Authentication Manager");
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),request.getPassword()
        ));
        LOGGER.info("Get User Details");
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        LOGGER.info("Issue Token");
        String token = jwtTokenHandler.generateNewToken(userDetails);
        return new AuthenticationResponse(token);
    }

}
