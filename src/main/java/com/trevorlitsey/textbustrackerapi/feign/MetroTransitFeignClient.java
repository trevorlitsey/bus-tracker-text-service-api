package com.trevorlitsey.textbustrackerapi.feign;

import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Departure;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Direction;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Route;
import com.trevorlitsey.textbustrackerapi.domain.metrotransit.Stop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="metrotransit", url="https://svc.metrotransit.org/NexTrip")
public interface MetroTransitFeignClient {
    @GetMapping("/Routes?format=json")
    List<Route> getRoutes();

    @RequestMapping(value="/Directions/{route}?format=json", method=RequestMethod.GET)
    List<Direction> getDirections(@PathVariable("route") String route);

    @GetMapping("/Stops/{route}/{direction}?format=json")
    List<Stop> getStops(@PathVariable("route") String route, @PathVariable("direction") String direction);

    @GetMapping("/{route}/{direction}/{stop}?format=json")
    List<Departure> getDepartures(@PathVariable("route") String route, @PathVariable("direction") String direction, @PathVariable("stop") String stop);
}
