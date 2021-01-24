package com.trevorlitsey.textbustrackerapi.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("twilio")
@Getter
public class TwilioConfig {
    String accountSSID;
    String authToken;
}
