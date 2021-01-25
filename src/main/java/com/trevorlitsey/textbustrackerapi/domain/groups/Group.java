package com.trevorlitsey.textbustrackerapi.domain.groups;

import com.trevorlitsey.textbustrackerapi.constants.GroupFields;
import com.trevorlitsey.textbustrackerapi.domain.shared.MongoDocument;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group extends MongoDocument {
    @Field(GroupFields.NAME)
    private String name;

    @Field(GroupFields.ROUTES)
    private List<Route> routes;

    @Field(GroupFields.USER_ID)
    private String userId;

    @Field(GroupFields.KEYWORDS)
    private List<String> keywords;
}
