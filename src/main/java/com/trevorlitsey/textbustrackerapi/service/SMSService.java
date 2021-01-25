package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.groups.Group;
import com.trevorlitsey.textbustrackerapi.domain.groups.Route;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Departure;
import com.trevorlitsey.textbustrackerapi.domain.sms.SMSRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.User;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SMSService {
    @Autowired
    GroupService groupService;

    @Autowired
    MetroTransitService metroTransitService;

    @Autowired
    UserService userService;

    public User registerPhoneNumber(String token, String phoneNumber) {
        return userService.registerUserPhoneNumber(token, phoneNumber);
    }

    public String getDeparturesFromSMS(String req) throws IOException {
        SMSRequest smsRequest = new SMSRequest(req);

        String phoneNumber = smsRequest.getFrom();
        String keyword = smsRequest.getBody().trim();

        User user = userService.findUserByPhoneNumber(phoneNumber);

        if (user == null) {
            return String.format("Sorry, no such user with phone number %s", phoneNumber);
        }

        if (keyword.isBlank()) {
            return "Keyword required";
        }

        List<Group> groups = groupService.findUserGroupsByUserIdAndKeyword(user.getId(), smsRequest.getBody());

        if (groups.isEmpty()) {
            return String.format("No groups found with keyword: %s", smsRequest.getBody());
        }

        StringBuilder msg = new StringBuilder();

        for (Group group : groups) {
            msg.append(formatDepartures(group)).append("\n");
        }

        return msg.toString();
    }

    private String formatDepartures(Group group) {
        StringBuilder departuresString = new StringBuilder();

        for (Route route : group.getRoutes()) {
            departuresString.append(route.getRoute().getLabel()).append("\n");
            departuresString.append(route.getStop().getLabel()).append("\n");
            departuresString.append(route.getDirection().getLabel()).append("\n");
            departuresString.append("--" + "\n");

            List<Departure> departures = metroTransitService.getDepartures(
                    route.getRoute().getValue(),
                    route.getDirection().getValue(),
                    route.getStop().getValue()
            );

            departuresString.append(departures
                    .stream()
                    .map(Departure::getDepartureText)
                    .collect(Collectors.joining(", "))).append("\n\n");
        }

        return departuresString.toString();
    }
}