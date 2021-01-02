package com.trevorlitsey.textbustrackerapi.web.controller;

import com.trevorlitsey.textbustrackerapi.domain.auth.DeleteAccountRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateAccountRequest;
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

    @PostMapping("/create-account")
    public AuthenticateResponse createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
         return authService.createAccount(createAccountRequest);
    }

    @DeleteMapping("/delete-account")
    public void deleteAccount(@RequestHeader(name = "Authorization") String authToken, @RequestBody DeleteAccountRequest deleteAccountRequest) {
        authService.deleteAccount(deleteAccountRequest, authUtils.getUserIdFromToken(authToken));
    }
}
