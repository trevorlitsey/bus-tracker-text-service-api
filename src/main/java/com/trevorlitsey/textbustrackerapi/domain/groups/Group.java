package com.trevorlitsey.textbustrackerapi.domain.groups;

import com.trevorlitsey.textbustrackerapi.constants.GroupFields;
import com.trevorlitsey.textbustrackerapi.domain.shared.MongoDocument;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
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
