package com.trevorlitsey.poolpractice.domain;

import com.trevorlitsey.poolpractice.types.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User extends MongoDocument {
    private String email;

    private String password;

    private String phoneNumber;

    private List<Permission> permissions;
}
