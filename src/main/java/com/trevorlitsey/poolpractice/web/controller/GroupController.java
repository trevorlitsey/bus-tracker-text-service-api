package com.trevorlitsey.poolpractice.web.controller;

import com.trevorlitsey.poolpractice.domain.Group;
import com.trevorlitsey.poolpractice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    GroupService groupService;

    @GetMapping
    public List<com.trevorlitsey.poolpractice.domain.Group> getGroups() {
        return groupService.findAllGroups();
    }

    @PostMapping
    public com.trevorlitsey.poolpractice.domain.Group postGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @PatchMapping("/{id}")
    public com.trevorlitsey.poolpractice.domain.Group putGroup(@PathVariable String id, @RequestBody Group group) {
        return groupService.patchRoutine(id, group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable String id) {
        groupService.deleteRoutine(id);
    }
}
