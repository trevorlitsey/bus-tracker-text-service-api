package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest;
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateRequest;
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateResponse;
import com.trevorlitsey.textbustrackerapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(), authenticateRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Incorrect email and/or password"
            );
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticateRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthenticateResponse(jwt);
    }

    public AuthenticateResponse createUser(CreateUserRequest createUserRequest) {
        UserDetails userDetails = userDetailsService.createUser(createUserRequest);
        return authenticate(new AuthenticateRequest(userDetails.getUsername(), createUserRequest.getPassword()));
    }

    public void deleteUser(String id, String userId) {
        userDetailsService.deleteUser(id, userId);
    }
}
