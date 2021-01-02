package com.trevorlitsey.textbustrackerapi.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticateRequest {
    private final String email;
    private final String password;
}
