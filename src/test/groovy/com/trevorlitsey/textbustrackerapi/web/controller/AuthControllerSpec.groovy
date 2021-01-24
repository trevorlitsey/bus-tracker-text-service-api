package com.trevorlitsey.textbustrackerapi.web.controller

import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateRequest
import com.trevorlitsey.textbustrackerapi.domain.auth.AuthenticateResponse
import com.trevorlitsey.textbustrackerapi.domain.users.CreateUserRequest
import com.trevorlitsey.textbustrackerapi.service.AuthService
import com.trevorlitsey.textbustrackerapi.utils.AuthUtils
import spock.lang.Specification

class AuthControllerSpec extends Specification {
    AuthService authService = Mock(AuthService)

    AuthUtils authUtils = Mock(AuthUtils)

    AuthController authController = new AuthController(authService: authService, authUtils: authUtils)

    def 'should authenticate user'() {
        setup:
        def email = 'foo@foo.com'
        def password = 'passw0rd!'
        def authenticateRequest = new AuthenticateRequest(email, password)
        def authenticateResponse = new AuthenticateResponse('jwt123')

        when:
        def result = authController.authenticate(authenticateRequest)

        then:
        1 * authService.authenticate(authenticateRequest) >> authenticateResponse
        result == authenticateResponse
    }

    def 'should create user'() {
        setup:
        def email = 'foo@foo.com'
        def password = 'passw0rd!'
        def createUserRequest = new CreateUserRequest(email, password)
        def authenticateResponse = new AuthenticateResponse('jwt123')

        when:
        def result = authController.createUser(createUserRequest)

        then:
        1 * authService.createUser(createUserRequest) >> authenticateResponse
        result == authenticateResponse

    }

    def 'should delete user'() {
        setup:
        def authToken = 'jwt123'
        def userId = '456'

        when:
        def result = authController.deleteUser(authToken, userId)

        then:
        1 * authUtils.getUserIdFromToken(authToken) >> userId
        1 * authService.deleteUser(userId, userId)
        result == null
    }
}
