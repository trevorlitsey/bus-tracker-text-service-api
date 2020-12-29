package com.trevorlitsey.poolpractice.service;

import com.trevorlitsey.poolpractice.domain.Group;
import com.trevorlitsey.poolpractice.domain.Route;
import com.trevorlitsey.poolpractice.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Group createGroup(Group group) {
        if (group.getName() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Group must have a name"
            );
        }

        if (group.getUserId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Group must have a user id"
            );
        }

        return groupRepository.insert(group);
    }

    public Group patchRoutine(String id, Group group) {
        Optional<Group> optionalRoutineToUpdate = groupRepository.findById(id);

        if (optionalRoutineToUpdate.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Group not found"
            );
        }

        Group groupToUpdate = optionalRoutineToUpdate.get();

        String name = group.getName();
        List<Route> routes = group.getRoutes();
        List<String> keywords = group.getKeywords();

        if (name != null) {
            groupToUpdate.setName(name);
        }

        if (routes != null) {
            groupToUpdate.setRoutes(routes);
        }

        if (keywords != null) {
            groupToUpdate.setKeywords(keywords);
        }

        return groupRepository.save(groupToUpdate);
    }

    public void deleteRoutine(String id) {
        groupRepository.deleteById(id);
    }
}
