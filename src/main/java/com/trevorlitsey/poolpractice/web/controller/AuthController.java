package com.trevorlitsey.poolpractice.web.controller;

import com.trevorlitsey.poolpractice.domain.CreateUserRequest;
import com.trevorlitsey.poolpractice.domain.LoginRequest;
import com.trevorlitsey.poolpractice.domain.LoginResponse;
import com.trevorlitsey.poolpractice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

     @PostMapping("/login")
     public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/create-user")
    public LoginResponse createAccount(@RequestBody CreateUserRequest createUserRequest) {
         return authService.createUser(createUserRequest);
    }
}
