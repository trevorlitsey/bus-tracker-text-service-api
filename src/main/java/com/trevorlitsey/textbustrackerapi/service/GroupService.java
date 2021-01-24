package com.trevorlitsey.textbustrackerapi.service;

import com.trevorlitsey.textbustrackerapi.constants.Collections;
import com.trevorlitsey.textbustrackerapi.constants.GroupFields;
import com.trevorlitsey.textbustrackerapi.domain.groups.Group;
import com.trevorlitsey.textbustrackerapi.domain.groups.Route;
import com.trevorlitsey.textbustrackerapi.repositories.GroupRepository;
import org.springframework.beans.BeanUtils;
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
                Query.query(Criteria.where(GroupFields.USER_ID).is(userId)),
                Group.class,
                Collections.GROUP
        );
    }

    public Group createGroup(Group group, String userId) {
        String name = group.getName();

        if (name == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Group must have a name"
            );
        }

        Group existingGroup = mongoOperations.findOne(
                Query.query(Criteria.where(GroupFields.NAME).is(name).and(GroupFields.USER_ID).is(userId)),
                Group.class,
                Collections.GROUP
        );

        if (existingGroup != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Group with name %s already exists", name)
            );
        }

        group.setUserId(userId);

        return groupRepository.insert(group);
    }

    public Group patchGroup (String id, Group group, String userId) {
        Optional<Group> optionalGroupToUpdate = groupRepository.findById(id);

        if (optionalGroupToUpdate.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Group not found"
            );
        }

        Group existingGroup = optionalGroupToUpdate.get();

        if (!existingGroup.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, String.format("User not allowed to modify group with id %s", id)
            );
        }

        Group groupToUpdate = Group.builder().build();

        BeanUtils.copyProperties(existingGroup, groupToUpdate);

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

        if (groupToDelete.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Group not found"
            );
        }


        if (!groupToDelete.get().getUserId().equals(userId) ) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, String.format("User not authorized to delete group with id %s", id)
            );
        }

        groupRepository.deleteById(id);
    }

    public void deleteUserGroups(String userId) {
        List<Group> groupsToDelete = mongoOperations.find(
                Query.query(Criteria.where(GroupFields.USER_ID).is(userId)),
                Group.class,
                Collections.GROUP
        );

        groupRepository.deleteAll(groupsToDelete);
    }
}
