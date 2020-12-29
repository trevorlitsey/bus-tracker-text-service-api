package com.trevorlitsey.poolpractice.repositories;

import com.trevorlitsey.poolpractice.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
