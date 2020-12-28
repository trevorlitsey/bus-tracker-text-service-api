package com.trevorlitsey.poolpractice.web.controller;

import com.trevorlitsey.poolpractice.domain.AuthRequest;
import com.trevorlitsey.poolpractice.domain.AuthResponse;
import com.trevorlitsey.poolpractice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    AuthService authService;

     @PostMapping("/login")
     public AuthResponse login(@RequestBody AuthRequest authRequest){
        return authService.login(authRequest);
    }
}
