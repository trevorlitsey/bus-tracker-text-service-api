package com.trevorlitsey.textbustrackerapi.repositories;

import com.trevorlitsey.textbustrackerapi.domain.groups.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
