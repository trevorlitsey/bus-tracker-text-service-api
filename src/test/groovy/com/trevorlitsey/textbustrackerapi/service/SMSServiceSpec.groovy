package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.domain.groups.Group
import com.trevorlitsey.textbustrackerapi.domain.groups.Route
import com.trevorlitsey.textbustrackerapi.domain.groups.RouteData
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Departure
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
        def req = "From=$phoneNumber&Body=$message"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> new User()
        result == "Keyword required"
    }

    def 'test getDeparturesFromSMS - no groups found'() {
        setup:
        def userId = "123"
        def user = new User()
        user.setId(userId)
        def message = "some keyword"
        def phoneNumber = '1234567890'
        def req = "From=$phoneNumber&Body=$message"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> user
        1 * groupService.findUserGroupsByUserIdAndKeyword(userId, message) >> List.of()
        result == String.format("No groups found with keyword: %s", message)
    }

    def 'test getDeparturesFromSMS - format departures, no upcoming times'() {
        setup:
        def userId = "123"
        def user = new User()
        user.setId(userId)
        def message = "some keyword"
        def phoneNumber = '1234567890'
        def req = "From=$phoneNumber&Body=$message"
        def routeLabel = "18 Bus"
        def routeValue = "18"
        def directionLabel = "SOUTHBOUND"
        def directionValue = "south"
        def stopLabel = "46th and Grand"
        def stopValue = "46-and-grand"
        def group = Group.builder().routes(List.of(
                new Route(
                        new RouteData(routeLabel, routeValue),
                        new RouteData(directionLabel, directionValue),
                        new RouteData(stopLabel, stopValue)
                )
        )).build()
        def expected =
                "$routeLabel\n" +
                        "$directionLabel\n" +
                        "$stopLabel\n" +
                        "--\n" +
                        "No upcoming departures\n" +
                        "\n"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> user
        1 * groupService.findUserGroupsByUserIdAndKeyword(userId, message) >> List.of(group)
        1 * metroTransitService.getDepartures(
                routeValue,
                directionValue,
                stopValue,
        ) >> List.of()
        result == expected
    }

    def 'test getDeparturesFromSMS - format departures, some upcoming times'() {
        setup:
        def userId = "123"
        def user = new User()
        user.setId(userId)
        def message = "some keyword"
        def phoneNumber = '1234567890'
        def req = "From=$phoneNumber&Body=$message"
        def routeLabel = "18 Bus"
        def routeValue = "18"
        def directionLabel = "SOUTHBOUND"
        def directionValue = "south"
        def stopLabel = "46th and Grand"
        def stopValue = "46-and-grand"
        def group = Group.builder().routes(List.of(
                new Route(
                        new RouteData(routeLabel, routeValue),
                        new RouteData(directionLabel, directionValue),
                        new RouteData(stopLabel, stopValue)
                )
        )).build()
        def departureText1 = "4m"
        def departureText2 = "8:25"
        def departure1 = Departure.builder().departureText(departureText1).build()
        def departure2 = Departure.builder().departureText(departureText2).build()
        def expected =
                "$routeLabel\n" +
                        "$directionLabel\n" +
                        "$stopLabel\n" +
                        "--\n" +
                        "$departureText1, $departureText2\n" +
                        "\n"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> user
        1 * groupService.findUserGroupsByUserIdAndKeyword(userId, message) >> List.of(group)
        1 * metroTransitService.getDepartures(
                routeValue,
                directionValue,
                stopValue,
        ) >> List.of(departure1, departure2)
        result == expected
    }

    def 'test getDeparturesFromSMS - format departures, multiple routes'() {
        setup:
        def userId = "123"
        def user = new User()
        user.setId(userId)
        def message = "some keyword"
        def phoneNumber = '1234567890'
        def req = "From=$phoneNumber&Body=$message"
        def routeLabel = "18 Bus"
        def routeValue = "18"
        def directionLabel = "SOUTHBOUND"
        def directionValue = "south"
        def stopLabel = "46th and Grand"
        def stopValue = "46-and-grand"
        def group = Group.builder().routes(List.of(
                new Route(
                        new RouteData(routeLabel, routeValue),
                        new RouteData(directionLabel, directionValue),
                        new RouteData(stopLabel, stopValue)
                ),
                new Route(
                        new RouteData(routeLabel, routeValue),
                        new RouteData(directionLabel, directionValue),
                        new RouteData(stopLabel, stopValue)
                )
        )).build()
        def departureText1 = "4m"
        def departureText2 = "8:25"
        def departure1 = Departure.builder().departureText(departureText1).build()
        def departure2 = Departure.builder().departureText(departureText2).build()
        def expected =
                "$routeLabel\n" +
                        "$directionLabel\n" +
                        "$stopLabel\n" +
                        "--\n" +
                        "$departureText1, $departureText2\n" +
                        "\n" +
                        "$routeLabel\n" +
                        "$directionLabel\n" +
                        "$stopLabel\n" +
                        "--\n" +
                        "$departureText1, $departureText2\n" +
                        "\n"

        when:
        def result = smsService.getDeparturesFromSMS(req)

        then:
        1 * userService.findUserByPhoneNumber(phoneNumber) >> user
        1 * groupService.findUserGroupsByUserIdAndKeyword(userId, message) >> List.of(group)
        2 * metroTransitService.getDepartures(
                routeValue,
                directionValue,
                stopValue,
        ) >> List.of(departure1, departure2)
        result == expected
    }
}
