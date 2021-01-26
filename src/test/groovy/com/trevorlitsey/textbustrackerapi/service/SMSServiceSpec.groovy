package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.domain.sms.SMSRequest
import com.trevorlitsey.textbustrackerapi.domain.users.User
import spock.lang.Specification

class SMSServiceSpec extends Specification {
    GroupService groupService = Mock(GroupService)

    MetroTransitService metroTransitService = Mock(MetroTransitService)

    UserService userService = Mock(UserService)

    SMSService smsService = new SMSService(
            groupService: groupService,
            metroTransitService: metroTransitService,
            userService: userService
    )

    def 'test registerPhoneNumber'() {
        setup:
        def token = 'abc123'
        def phoneNumber = '1234567890'

        when:
        smsService.registerPhoneNumber(token, phoneNumber)

        then:
        1 * userService.registerUserPhoneNumber(token, phoneNumber)
    }

    def 'test getDeparturesFromSMS - user does not exist'() {
        setup:
        def phoneNumber = '1234567890'
        def req = "From=$phoneNumber"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> null
        result == String.format("Sorry, no such user with phone number %s", phoneNumber)
    }

    def 'test getDeparturesFromSMS - message is blank'() {
        setup:
        def message = " "
        def phoneNumber = '1234567890'
        def req = "From=$phoneNumber&Message=$message"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> new User()
        result == "Keyword required"
    }

    def 'test getDeparturesFromSMS - no groups found'() {
        // TODO
    }

    def 'test getDeparturesFromSMS - format departures'() {
        // TODO
    }
}
