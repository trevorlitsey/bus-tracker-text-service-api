package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.auth.DeleteAccountRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.CreateAccountRequest;
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

    public UserDetails createUser(CreateAccountRequest createAccountRequest) {
        User existingUserWithEmail = userService.findUserByEmail(createAccountRequest.getEmail());

        if (existingUserWithEmail != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already registered"
            );
        }

        CreateAccountRequest createAccountRequestWithEncryptedPassword = new CreateAccountRequest(
                createAccountRequest.getEmail(),
                passwordConfig.encode(createAccountRequest.getPassword()),
                createAccountRequest.getPhoneNumber()
        );

        User user = userService.createUser(createAccountRequestWithEncryptedPassword);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public void deleteAccount(DeleteAccountRequest deleteAccountRequest, String userId) {
        String id = deleteAccountRequest.getUserId();

        if (!id.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, String.format("User not authorized to delete user with id %s", id)
            );
        }

        userService.deleteUser(deleteAccountRequest.getUserId());
    }
}
