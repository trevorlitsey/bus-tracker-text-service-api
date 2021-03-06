package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest
import com.trevorlitsey.textbustrackerapi.domain.users.User
import com.trevorlitsey.textbustrackerapi.utils.PasswordConfig
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

class UserDetailsServiceSpec extends Specification {
    GroupService groupService = Mock(GroupService)

    PasswordConfig passwordConfig = Mock(PasswordConfig)

    UserService userService = Mock(UserService)

    UserDetailsService userDetailsService = new UserDetailsService(groupService: groupService, passwordConfig: passwordConfig, userService: userService)

    def 'should load user by username'() {
        setup:
        def email = 'foo@foo.com'
        def password = 'abc123'
        def user = User.builder().email(email).password(password).build()
        def expectedUser = new org.springframework.security.core.userdetails.User(
                email,
                password,
                new ArrayList<>()
        )

        when:
        def result = userDetailsService.loadUserByUsername(email)

        then:
        1 * userService.findUserByEmail(email) >> user
        result == expectedUser

        when: 'user does not exist'
        def userDoesNotExistResult = userDetailsService.loadUserByUsername(email)

        then: 'should throw error'
        1 * userService.findUserByEmail(email) >> null
        userDoesNotExistResult == null
        def err = thrown(UsernameNotFoundException)
        err.getMessage() == String.format("Email %s not found", email)
    }

    def 'should create user'() {
        setup:
        def email = 'foo@foo.com'
        def password = '123xyz'

        def encodedPassword = '123beepboopbeep'
        CreateUserRequest createdUser
        def expectedUserDetails =  new org.springframework.security.core.userdetails.User(
                email,
                password,
                new ArrayList<>()
        )

        when:
        def result = userDetailsService.createUser(new CreateUserRequest(email, password))

        then:
        1 * userService.findUserByEmail(email) >> null
        1 * passwordConfig.encode(password) >> encodedPassword
        1 * userService.createUser(_) >> {
            createdUser = it[0]
            return new User(email, password, null, null, null, List.of())
        }
        createdUser.getEmail() == email
        createdUser.getPassword() == encodedPassword
        result == expectedUserDetails

        when: 'user already exists'
        def userAlreadyExistsResult = userDetailsService.createUser(new CreateUserRequest(email, password))

        then: 'should throw error'
        1 * userService.findUserByEmail(email) >> new User()
        0 * passwordConfig.encode
        0 * userService.createUser
        userAlreadyExistsResult == null
        def err = thrown(ResponseStatusException)
        err.getStatus() == HttpStatus.BAD_REQUEST
        err.getMessage().contains(String.format("Email %s is already registered", email))
    }

    def 'should delete user'() {
        setup:
        def userId = '123'

        when:
        def result = userDetailsService.deleteUser(userId, userId)

        then:
        result == null
        1 * userService.deleteUser(userId)
        1 * groupService.deleteUserGroups(userId)

        when: 'user id from auth token does not match'
        def userIdDoesNotMatchResult = userDetailsService.deleteUser(userId, 'not123')

        then: 'should throw error'
        userIdDoesNotMatchResult == null
        0 * userService.deleteUser
        0 * groupService.deleteUserGroups
        def err = thrown(ResponseStatusException)
        err.getStatus() == HttpStatus.UNAUTHORIZED
        err.getMessage().contains(String.format("User not authorized to delete user with id %s", userId))
    }
}
