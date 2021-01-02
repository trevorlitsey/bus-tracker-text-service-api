package com.trevorlitsey.textbustrackerapi.repositories;

import com.trevorlitsey.textbustrackerapi.domain.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
