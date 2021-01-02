package com.trevorlitsey.textbustrackerapi.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteAccountRequest {
    private final String userId;
}
