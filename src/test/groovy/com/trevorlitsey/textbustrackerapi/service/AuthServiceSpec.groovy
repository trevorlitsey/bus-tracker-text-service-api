package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateRequest
import com.trevorlitsey.textbustrackerapi.domain.auth.DeleteAccountRequest
import com.trevorlitsey.textbustrackerapi.domain.users.CreateAccountRequest
import com.trevorlitsey.textbustrackerapi.utils.JwtUtil
import org.mockito.InjectMocks
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
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

    def 'should create account'() {
        setup:
        def email = "foo@foo.com"
        def phoneNumber = "123-456-7890"
        def password = "abc123"
        def createAccountRequest = new CreateAccountRequest(email, phoneNumber, password)
        def userDetails = new User(
                email,
                password,
                new ArrayList<>()
        )
        def jwt = '123'

        when:
        def result = authService.createAccount(createAccountRequest)

        then:
        1 * userDetailsService.createUser(createAccountRequest) >> userDetails
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

    def 'should delete account'() {
        setup:
        def userId = "123"
        def deleteAccountRequest = new DeleteAccountRequest(userId)

        when:
        authService.deleteAccount(deleteAccountRequest, userId)

        then:
        1 * userDetailsService.deleteAccount(deleteAccountRequest, userId)
    }
}
