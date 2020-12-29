package com.trevorlitsey.poolpractice.service;

import com.trevorlitsey.poolpractice.domain.CreateUserRequest;
import com.trevorlitsey.poolpractice.domain.User;
import com.trevorlitsey.poolpractice.repositories.UserRepository;
import com.trevorlitsey.poolpractice.types.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mongoOperations.findOne(
                Query.query(Criteria.where("email").is(username)),
                User.class,
                "user"
        );

        if  (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public UserDetails createUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.getUsername(), createUserRequest.getPassword(), createUserRequest.getPhoneNumber(), List.of(Permission.USER));
        userRepository.insert(user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
