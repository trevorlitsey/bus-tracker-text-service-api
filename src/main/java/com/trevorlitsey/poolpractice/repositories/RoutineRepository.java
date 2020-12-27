package com.trevorlitsey.poolpractice.repositories;

import com.trevorlitsey.poolpractice.domain.Routine;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoutineRepository extends MongoRepository<Routine, String> {
}
