package com.trevorlitsey.poolpractice.domain;

import com.trevorlitsey.poolpractice.types.Permission;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends MongoDocument {
    private String email;

    private String password;

    private String phoneNumber;

    private List<Permission> permissions;
}
