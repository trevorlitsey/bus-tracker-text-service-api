package com.trevorlitsey.textbustrackerapi.domain.sms;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

public class SMSRequest {
    Map<String, String> properties;

    public SMSRequest(String req) {
        HashMap<String, String> properties = new HashMap<>();

        Arrays.stream(req.split("&")).forEach(keyValue -> {
            String[] keyValueSplit = keyValue.split("=");
            String key = keyValueSplit[0];
            String value = keyValueSplit[1];

            properties.put(key, URLDecoder.decode(value));
        });

        this.properties = properties;
    }

    public String get(String key) {
        return properties.get(key);
    }

    public String getFrom() {
        return properties.get("From");
    }

    public String getBody() {
        return properties.get("Body");
    }
}
