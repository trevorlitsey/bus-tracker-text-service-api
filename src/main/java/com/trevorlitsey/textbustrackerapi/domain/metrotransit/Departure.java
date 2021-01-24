package com.trevorlitsey.textbustrackerapi.domain.metrotransit;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.trevorlitsey.textbustrackerapi.constants.MetroTransitFields;
import lombok.Getter;

@Getter
public class Departure {
    @JsonSetter(MetroTransitFields.ACTUAL)
    Boolean actual;

    @JsonSetter(MetroTransitFields.BLOCK_NUMBER)
    Integer blockNumber;

    @JsonSetter(MetroTransitFields.DEPARTURE_TEXT)
    String departureText;

    @JsonSetter(MetroTransitFields.DEPARTURE_TIME)
    String departureTime;

    @JsonSetter(MetroTransitFields.DESCRIPTION)
    String description;

    @JsonSetter(MetroTransitFields.GATE)
    String gate;

    @JsonSetter(MetroTransitFields.ROUTE)
    String route;

    @JsonSetter(MetroTransitFields.ROUTE_DIRECTION)
    String routeDirection;

    @JsonSetter(MetroTransitFields.TERMINAL)
    String terminal;

    @JsonSetter(MetroTransitFields.VEHICLE_HEADING)
    Integer vehicleHeading;

    @JsonSetter(MetroTransitFields.VEHICLE_LATITUDE)
    Float vehicleLatitude;

    @JsonSetter(MetroTransitFields.VEHICLE_LONGITUDE)
    Float vehicleLongitude;
}
