package com.trevorlitsey.poolpractice.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Group extends MongoDocument {
    private String name;

    private List<Route> routes;

    private String userId;

    private List<String> keywords;
}
