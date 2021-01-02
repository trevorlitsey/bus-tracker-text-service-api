package com.trevorlitsey.textbustrackerapi.utils;

import com.trevorlitsey.textbustrackerapi.domain.users.User;
import com.trevorlitsey.textbustrackerapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    private UserService userService;

    public String getUserIdFromToken(String authToken) {
        String username = jwtUtil.extractUsername(authToken.substring(7));

        User user = userService.findUserByEmail(username);

        return user.getId();
    }
}
