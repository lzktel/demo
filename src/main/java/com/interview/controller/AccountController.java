package com.interview.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/api")
public class AccountController {
    static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping(path = "/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "test";
    }
}
