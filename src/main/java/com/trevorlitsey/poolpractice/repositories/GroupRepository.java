package com.trevorlitsey.poolpractice.repositories;

import com.trevorlitsey.poolpractice.domain.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
