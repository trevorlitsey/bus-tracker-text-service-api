package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.sms.SMSRequest;
import com.trevorlitsey.textbustrackerapi.domain.users.User;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SMSService {

   @Autowired
   UserService userService;

   public User registerPhoneNumber(String token, String phoneNumber) {
      return userService.registerUserPhoneNumber(token, phoneNumber);
   }

   public String getDepartures() {
      // TODO
       return "";
   }

   public String receiveSMS(String req) throws IOException {
      SMSRequest smsRequest = new SMSRequest(req);

      // get routes by phoneNumber

      Body body = new Body
              .Builder("Response")
              .build();
      com.twilio.twiml.messaging.Message sms = new com.twilio.twiml.messaging.Message
              .Builder()
              .body(body)
              .build();
      MessagingResponse twiml = new MessagingResponse
              .Builder()
              .message(sms)
              .build();
      return "hey!";
   }
}