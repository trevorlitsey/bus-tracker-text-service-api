package com.trevorlitsey.textbustrackerapi.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AuthenticateRequest {
    private final String email;
    private final String password;
}
