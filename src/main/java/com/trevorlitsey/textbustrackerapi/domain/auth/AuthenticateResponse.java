package com.trevorlitsey.textbustrackerapi.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticateResponse {
    private final String jwt;
}
