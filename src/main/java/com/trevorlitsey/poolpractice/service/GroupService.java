package com.trevorlitsey.poolpractice.service;

import com.trevorlitsey.poolpractice.domain.Group;
import com.trevorlitsey.poolpractice.domain.Route;
import com.trevorlitsey.poolpractice.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    MongoOperations mongoOperations;

    public List<Group> findAllGroups(String userId) {
        return mongoOperations.find(
                Query.query(Criteria.where("userId").is(userId)),
                Group.class,
                "group"
        );
    }

    public Group createGroup(Group group, String userId) {
        if (group.getName() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Group must have a name"
            );
        }

        group.setUserId(userId);

        return groupRepository.insert(group);
    }

    public Group patchRoutine(String id, Group group, String userId) {
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

    public void deleteGroup(String id, String userId) {
        Optional<Group> groupToDelete = groupRepository.findById(id);

        if (groupToDelete.isEmpty() || !groupToDelete.get().getUserId().equals(userId) ) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, String.format("User not authorized to delete group with id %s", id)
            );
        }

        groupRepository.deleteById(id);
    }
}
