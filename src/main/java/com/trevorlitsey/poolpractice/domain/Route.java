package com.trevorlitsey.poolpractice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Route {
    private RouteData route;
    private RouteData direction;
    private RouteData stop;
}
