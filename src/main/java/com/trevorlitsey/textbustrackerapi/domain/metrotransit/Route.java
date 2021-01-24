package com.trevorlitsey.textbustrackerapi.domain.metrotransit;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.trevorlitsey.textbustrackerapi.constants.MetroTransitFields;
import lombok.Getter;

@Getter
public class Route {
    @JsonSetter(MetroTransitFields.DESCRIPTION)
    String description;

    @JsonSetter(MetroTransitFields.PROVIDER_ID)
    String providerId;

    @JsonSetter(MetroTransitFields.ROUTE)
    String route;
}
