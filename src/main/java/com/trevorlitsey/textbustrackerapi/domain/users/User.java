package com.trevorlitsey.textbustrackerapi.domain.users;

import com.trevorlitsey.textbustrackerapi.constants.UserFields;
import com.trevorlitsey.textbustrackerapi.domain.shared.MongoDocument;
import com.trevorlitsey.textbustrackerapi.types.Permission;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends MongoDocument {
    @Field(UserFields.EMAIL)
    private String email;

    @Field(UserFields.PASSWORD)
    private String password;

    @Field(UserFields.PHONE_NUMBER)
    private String phoneNumber;

    @Field(UserFields.PERMISSIONS)
    private List<Permission> permissions;
}
