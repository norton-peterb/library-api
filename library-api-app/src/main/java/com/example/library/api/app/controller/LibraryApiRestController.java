package com.example.library.api.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class LibraryApiRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryApiRestController.class);

    @GetMapping(path = "/test",produces = MediaType.TEXT_HTML_VALUE)
    @Secured("ROLE_USER")
    public String testService() {
        return "<HTML><BODY><H1>Test Page OK</H1><HR></BODY></HTML>";
    }

}
