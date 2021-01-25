package com.trevorlitsey.textbustrackerapi.domain.groups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    private RouteData route;
    private RouteData direction;
    private RouteData stop;
}
