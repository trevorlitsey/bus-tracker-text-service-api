package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateRequest
import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest
import com.trevorlitsey.textbustrackerapi.utils.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.User
import spock.lang.Specification

class AuthServiceSpec extends Specification {
    AuthenticationManager authenticationManager = Mock(AuthenticationManager)

    UserDetailsService userDetailsService = Mock(UserDetailsService)

    JwtUtil jwtUtil = Mock(JwtUtil)

    AuthService authService = new AuthService(authenticationManager: authenticationManager, userDetailsService: userDetailsService, jwtUtil: jwtUtil)

    def 'should authenticate'() {
        setup:
        def email = "foo@foo.com"
        def password = "xyz123"
        def authenticateRequest = new AuthenticateRequest(email, password)
        def jwt = "123"
        def userDetails = new User(
                email,
                password,
                new ArrayList<>()
        )

        when:
        def result = authService.authenticate(authenticateRequest)

        then:
        1 * authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))
        1 * userDetailsService.loadUserByUsername(email) >> userDetails
        1 * jwtUtil.generateToken(userDetails) >> jwt
        result.getJwt() == jwt
        noExceptionThrown()

        when: 'authenticate fails'
        def failedResult = authService.authenticate(authenticateRequest)

        then: 'should throw error'
        1 * authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)) >> {
            throw new Error('oh no')
        }
        0 * userDetailsService.loadUserByUsername
        0 * jwtUtil.generateToken
        failedResult == null
        def e = thrown(Error)
        e.getMessage() == 'oh no'
    }

    def 'should create user'() {
        setup:
        def email = "foo@foo.com"
        def password = "abc123"
        def createUserRequest = new CreateUserRequest(email, password)
        def userDetails = new User(
                email,
                password,
                new ArrayList<>()
        )
        def jwt = '123'

        when:
        def result = authService.createUser(createUserRequest)

        then:
        1 * userDetailsService.createUser(createUserRequest) >> userDetails
        1 * authenticationManager.authenticate(_) >> {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken -> {
                usernamePasswordAuthenticationToken.getPrincipal() == email
                usernamePasswordAuthenticationToken.getCredentials() == password
                return
            }
        }
        1 * userDetailsService.loadUserByUsername(email) >> userDetails
        1 * jwtUtil.generateToken(userDetails) >> jwt
        result.getJwt() == jwt
        noExceptionThrown()
    }

    def 'should delete user'() {
        setup:
        def userId = "123"

        when:
        authService.deleteUser(userId, userId)

        then:
        1 * userDetailsService.deleteUser(userId, userId)
    }
}
