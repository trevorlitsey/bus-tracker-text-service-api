package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Departure;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Direction;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Route;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Stop;
import com.trevorlitsey.textbustrackerapi.feign.MetroTransitFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetroTransitService {
    @Autowired
    MetroTransitFeignClient metroTransitFeignClient;

    public List<Route> getRoutes() {
        return metroTransitFeignClient.getRoutes();
    }

    public List<Direction> getDirections(String route){
        return metroTransitFeignClient.getDirections(route);
    }

    public List<Stop> getStops(String route, String direction) {
        return metroTransitFeignClient.getStops(route, direction);
    }

    public List<Departure> getDepartures(String route, String direction, String stop) {
        return metroTransitFeignClient.getDepartures(route, direction, stop);
    }
}
