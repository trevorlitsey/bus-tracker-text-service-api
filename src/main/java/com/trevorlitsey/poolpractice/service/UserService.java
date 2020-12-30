package com.trevorlitsey.poolpractice.service;

import com.trevorlitsey.poolpractice.domain.CreateUserRequest;
import com.trevorlitsey.poolpractice.domain.Group;
import com.trevorlitsey.poolpractice.domain.User;
import com.trevorlitsey.poolpractice.repositories.UserRepository;
import com.trevorlitsey.poolpractice.types.Permission;
import com.trevorlitsey.poolpractice.utils.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    UserRepository userRepository;

    public User findUserByEmail(String email) {
        return mongoOperations.findOne(
                Query.query(Criteria.where("email").is(email)),
                User.class,
                "user"
        );
    }

    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getEmail(), createUserRequest.getPassword() , createUserRequest.getPhoneNumber(), List.of(Permission.USER));
        return userRepository.insert(user);
    }

    public void deleteUser(String id, String userId) {
        if (!id.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, String.format("User not authorized to delete user with id %s", id)
            );
        }

        userRepository.deleteById(id);
    }
}
