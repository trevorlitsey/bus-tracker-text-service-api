package com.trevorlitsey.textbustrackerapi.web.controller;

import com.trevorlitsey.textbustrackerapi.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/sms")
public class SMSController {
    @Autowired
    SMSService smsService;

    @PostMapping
    public String receiveSMS(@RequestBody String req) throws IOException {
        return smsService.getDeparturesFromSMS(req);
    }
}
