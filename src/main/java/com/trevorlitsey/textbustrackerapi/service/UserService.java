package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.constants.Collections;
import com.trevorlitsey.textbustrackerapi.constants.UserFields;
import com.trevorlitsey.textbustrackerapi.domain.groups.Group;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.User;
import com.trevorlitsey.textbustrackerapi.repositories.UserRepository;
import com.trevorlitsey.textbustrackerapi.types.Permission;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

    public User createUser(CreateUserRequest createUserRequest) {
        User user = User
                .builder()
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .phoneNumberRegistrationToken(getUniquePhoneNumberRegistrationToken())
                .phoneNumberRegistrationTokenExpiration(getPhoneNumberRegistrationTokenExpiration())
                .permissions(List.of(Permission.USER))
                .build();

        return userRepository.insert(user);
    }

    public User generateNewPhoneNumberRegistrationToken(String userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("No such user %s", userId)
            );
        }

        User userToSave = User.builder().build();

        BeanUtils.copyProperties(user, userToSave);

        userToSave.setPhoneNumberRegistrationToken(getUniquePhoneNumberRegistrationToken());
        userToSave.setPhoneNumberRegistrationTokenExpiration(getPhoneNumberRegistrationTokenExpiration());

        return userRepository.save(userToSave);
    }

    public User registerUserPhoneNumber(String token, String phoneNumber) {
        User user = mongoOperations.findOne(
                Query.query(Criteria.where(UserFields.PHONE_NUMBER_REGISTRATION_TOKEN).is(token)),
                User.class,
                Collections.USER
        );

        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Invalid token %s", token)
            );
        }

        User userToSave = User.builder().build();

        BeanUtils.copyProperties(user, userToSave);

        userToSave.setPhoneNumber(phoneNumber);
        userToSave.setPhoneNumberRegistrationToken(null);
        userToSave.setPhoneNumberRegistrationTokenExpiration(null);

        return userRepository.save(user);
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

    private String getUniquePhoneNumberRegistrationToken() {
        String token = RandomStringUtils.random(4);

        User userWithToken = mongoOperations.findOne(
                Query.query(Criteria.where(UserFields.PHONE_NUMBER_REGISTRATION_TOKEN).is(token)),
                User.class,
                Collections.USER
        );

        if (userWithToken == null) {
            return token;
        }

        return getUniquePhoneNumberRegistrationToken();
    }

    private LocalDateTime getPhoneNumberRegistrationTokenExpiration() {
        return LocalDateTime.now().plusMinutes(15);
    }
}
