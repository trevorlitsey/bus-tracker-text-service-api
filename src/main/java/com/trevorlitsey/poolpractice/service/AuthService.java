package com.trevorlitsey.poolpractice.service;

import com.trevorlitsey.poolpractice.domain.CreateUserRequest;
import com.trevorlitsey.poolpractice.domain.LoginRequest;
import com.trevorlitsey.poolpractice.domain.LoginResponse;
import com.trevorlitsey.poolpractice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) {
        // TODO: set custom message if method throws?
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        return new LoginResponse(jwt);
    }

    public LoginResponse createUser(CreateUserRequest createUserRequest) {
        userDetailsService.createUser(createUserRequest);
        return login(new LoginRequest(createUserRequest.getUsername(), createUserRequest.getPassword()));
    }
}
