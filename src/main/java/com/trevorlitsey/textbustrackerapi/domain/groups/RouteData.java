package com.trevorlitsey.textbustrackerapi.domain.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RouteData {
    private String label;
    private String userLabel;
    private String value;
}
