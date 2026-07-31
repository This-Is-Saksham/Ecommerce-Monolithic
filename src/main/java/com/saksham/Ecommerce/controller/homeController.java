package com.saksham.Ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class homeController {

    @GetMapping("/")
    public String home(){
        return "Hello World";
    }

    @GetMapping("/login")
    public String login() {
        return "logged in";
    }
}