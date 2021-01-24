package com.trevorlitsey.textbustrackerapi.web.controller;

import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Departure;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Direction;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Route;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Stop;
import com.trevorlitsey.textbustrackerapi.service.MetroTransitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("metro-transit")
public class MetroTransitController {
    @Autowired
    MetroTransitService metroTransitService;

    @GetMapping("/routes")
    public List<Route> getRoutes() {
        return metroTransitService.getRoutes();
    }

    @GetMapping("/directions/{route}")
    public List<Direction> getDirections(@PathVariable String route) {
        return metroTransitService.getDirections(route);
    }

    @GetMapping("/stops/{route}/{direction}")
    public List<Stop> getStops(@PathVariable String route, @PathVariable String direction) {
        return metroTransitService.getStops(route, direction);
    }

    @GetMapping("/departures/{route}/{direction}/{stop}")
    public List<Departure> getDepartures(@PathVariable String route, @PathVariable String direction, @PathVariable String stop) {
        return metroTransitService.getDepartures(route, direction, stop);
    }
}
