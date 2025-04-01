package com.Security.Security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secure")
public class SecureController {

    @GetMapping("/hello")
    public String helloSecure() {
        return "Hello from a protected endpoint!";
    }
}