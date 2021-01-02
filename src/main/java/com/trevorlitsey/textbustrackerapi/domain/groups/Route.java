package com.trevorlitsey.textbustrackerapi.domain.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Route {
    private RouteData route;
    private RouteData direction;
    private RouteData stop;
}
