package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.auth.DeleteAccountRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateAccountRequest;
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateRequest;
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateResponse;
import com.trevorlitsey.textbustrackerapi.utils.JwtUtil;
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

  public AuthenticateResponse authenticate(AuthenticateRequest authenticateRequest) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(), authenticateRequest.getPassword()));

    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticateRequest.getEmail());

    final String jwt = jwtUtil.generateToken(userDetails);

    return new AuthenticateResponse(jwt);
  }

  public AuthenticateResponse createAccount(CreateAccountRequest createAccountRequest) {
    UserDetails userDetails=userDetailsService.createUser(createAccountRequest);
    return authenticate(new AuthenticateRequest(userDetails.getUsername(), createAccountRequest.getPassword()));
  }

  public void deleteAccount(DeleteAccountRequest deleteAccountRequest, String userId) {
    userDetailsService.deleteAccount(deleteAccountRequest, userId);
  }
}
