package com.trevorlitsey.textbustrackerapi.web.controller;

import com.trevorlitsey.textbustrackerapi.constants.Headers;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest;
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateRequest;
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateResponse;
import com.trevorlitsey.textbustrackerapi.service.AuthService;
import com.trevorlitsey.textbustrackerapi.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    AuthUtils authUtils;

    @PostMapping("/authenticate")
    public AuthenticateResponse authenticate(@RequestBody AuthenticateRequest authenticateRequest){
        return authService.authenticate(authenticateRequest);
    }

    @PostMapping("/create-user")
    public AuthenticateResponse createUser(@RequestBody CreateUserRequest createUserRequest) {
         return authService.createUser(createUserRequest);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteUser(@RequestHeader(name = Headers.AUTHORIZATION) String authToken, @PathVariable String id) {
        authService.deleteUser(id, authUtils.getUserIdFromToken(authToken));
    }
}
