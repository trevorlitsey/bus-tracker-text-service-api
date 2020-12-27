package com.trevorlitsey.poolpractice.web.controller;

import com.trevorlitsey.poolpractice.domain.Routine;
import com.trevorlitsey.poolpractice.service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routines")
public class RoutineController {
    @Autowired
    RoutineService routineService;

    @GetMapping("")
    public List<Routine> routines() {
        return routineService.findAllRoutines();
    }

    @PostMapping("")
    public Routine createRoutine(@RequestBody Routine routine) {
        return routineService.createRoutine(routine);
    }

    @PutMapping("/{id}")
    public Routine putRoutine(@PathVariable String id, @RequestBody Routine routine) {
        return routineService.updateRoutine(id, routine);
    }

    @DeleteMapping("/{id}")
    public void createRoutine(@PathVariable String id) {
        routineService.deleteRoutine(id);
    }
}
