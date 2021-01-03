package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.auth.DeleteUserRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.User;
import com.trevorlitsey.textbustrackerapi.utils.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    GroupService groupService;

    @Autowired
    PasswordConfig passwordConfig;

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("Email %s not found", username));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    public UserDetails createUser(CreateUserRequest createUserRequest) {
        User existingUserWithEmail = userService.findUserByEmail(createUserRequest.getEmail());

        if (existingUserWithEmail != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already registered"
            );
        }

        CreateUserRequest createUserRequestWithEncryptedPassword = new CreateUserRequest(
                createUserRequest.getEmail(),
                passwordConfig.encode(createUserRequest.getPassword()),
                createUserRequest.getPhoneNumber()
        );

        User user = userService.createUser(createUserRequestWithEncryptedPassword);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public void deleteUser(DeleteUserRequest deleteUserRequest, String userId) {
        String id = deleteUserRequest.getUserId();

        if (!id.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, String.format("User not authorized to delete user with id %s", id)
            );
        }

        userService.deleteUser(deleteUserRequest.getUserId());
        groupService.deleteUserGroups(userId);
    }
}
