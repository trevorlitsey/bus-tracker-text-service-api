package com.trevorlitsey.poolpractice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Routine {
    @Id
    @Getter
    @Setter
    private String id;

    @CreatedDate
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Getter
    private LocalDateTime lastUpdatedAt;

    @Getter
    @Setter
    private String name;
}
