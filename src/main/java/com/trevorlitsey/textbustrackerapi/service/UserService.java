package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.constants.Collections;
import com.trevorlitsey.textbustrackerapi.constants.UserFields;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateAccountRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.User;
import com.trevorlitsey.textbustrackerapi.repositories.UserRepository;
import com.trevorlitsey.textbustrackerapi.types.Permission;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {
    @Autowired
    final MongoOperations mongoOperations;

    @Autowired
    final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return mongoOperations.findOne(
                Query.query(Criteria.where(UserFields.EMAIL).is(email)),
                User.class,
                Collections.USER
        );
    }

    public User createUser(CreateAccountRequest createAccountRequest) {
        User user = new User(createAccountRequest.getEmail(), createAccountRequest.getPassword() , createAccountRequest.getPhoneNumber(), List.of(Permission.USER));
        return userRepository.insert(user);
    }

    public void deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("User does not exist with id %s", id)
            );
        }

        userRepository.delete(user.get());
    }
}
