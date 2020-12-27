package com.trevorlitsey.poolpractice.service;

import com.trevorlitsey.poolpractice.domain.Routine;
import com.trevorlitsey.poolpractice.repositories.RoutineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RoutineService {
    @Autowired
    RoutineRepository routineRepository;

    public List<Routine> findAllRoutines() {
        return routineRepository.findAll();
    };

    public Routine createRoutine(Routine routine) {
        return routineRepository.insert(routine);
    }

    public Routine updateRoutine(String id, Routine routine) {
        Optional<Routine> optionalRoutineToUpdate = routineRepository.findById(id);

        Routine routineToUpdate = optionalRoutineToUpdate.get();

        if (optionalRoutineToUpdate == null) {
            // TODO - how to throw 404?
            throw new Error("Routine not found for id: " + id);
        }

        routineToUpdate.setName(routine.getName());

        return routineRepository.save(routineToUpdate);
    }

    public void deleteRoutine(String id) {
        routineRepository.deleteById(id);
    }
}
