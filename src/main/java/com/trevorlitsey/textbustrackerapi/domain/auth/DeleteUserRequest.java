package com.trevorlitsey.textbustrackerapi.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteUserRequest {
    private final String userId;
}
