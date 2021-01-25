package com.trevorlitsey.textbustrackerapi.domain.metrotransit;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.trevorlitsey.textbustrackerapi.constants.MetroTransitFields;
import lombok.Getter;

@Getter
public class Stop {
    @JsonSetter(MetroTransitFields.TEXT)
    String label;

    @JsonSetter(MetroTransitFields.VALUE)
    String value;
}
