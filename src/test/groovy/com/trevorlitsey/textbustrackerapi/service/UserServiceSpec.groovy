package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.constants.Collections
import com.trevorlitsey.textbustrackerapi.constants.UserFields
import com.trevorlitsey.textbustrackerapi.domain.users.CreateAccountRequest
import com.trevorlitsey.textbustrackerapi.domain.users.User
import com.trevorlitsey.textbustrackerapi.repositories.UserRepository
import com.trevorlitsey.textbustrackerapi.types.Permission
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import spock.lang.*

class UserServiceSpec extends Specification {
    MongoOperations mongoOperations = Mock(MongoOperations)

    UserRepository userRepository = Mock(UserRepository)

    UserService userService = new UserService(mongoOperations, userRepository)

    def 'should find user by email'() {
        setup:
        def email = "foo@foo.com"
        def user = new User()

        when:
        def result = userService.findUserByEmail(email)

        then:
        1 * mongoOperations.findOne(
                Query.query(Criteria.where(UserFields.EMAIL).is(email)),
                User.class,
                Collections.USER) >> user
        result == user
    }

    def 'should create user'() {
        setup:
        def email = 'foo@foo.com'
        def password = '123xyz'
        def phoneNumber = '123-456-7890'
        def createAccountRequest = new CreateAccountRequest(email, password, phoneNumber)
        def expectedUser = new User(
                email,
                password,
                phoneNumber,
                List.of(Permission.USER)
        )

        when:
        def result = userService.createUser(createAccountRequest)

        then:
        1 * userRepository.insert(_ as User) >> {
            User userToInsert ->
                {

                    userToInsert.getEmail() == email
                    userToInsert.getPassword() == password
                    userToInsert.getPhoneNumber() == phoneNumber
                    userToInsert.getPermissions() == List.of(Permission.USER)
                    return expectedUser
                }
        }
        result == expectedUser
    }

    def 'should delete user'() {
        setup:
        def id = '123'
        def user = new User()

        when: 'user exists'
        userService.deleteUser(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(user)
        1 * userRepository.delete(user)
        noExceptionThrown()

        when: 'user does not exist'
        userService.deleteUser(id)

        then: 'throw error'
        1 * userRepository.findById(id) >> Optional.ofNullable(null)
        0 * userRepository.delete(user)
        def e = thrown ResponseStatusException
        e.getStatus() == HttpStatus.BAD_REQUEST
        e.getMessage().contains(String.format("User does not exist with id %s", id))
    }
}
