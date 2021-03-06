package com.trevorlitsey.textbustrackerapi.web.controller;

import com.trevorlitsey.textbustrackerapi.constants.Headers;
import com.trevorlitsey.textbustrackerapi.domain.groups.Group;
import com.trevorlitsey.textbustrackerapi.service.GroupService;
import com.trevorlitsey.textbustrackerapi.service.UserService;
import com.trevorlitsey.textbustrackerapi.utils.AuthUtils;
import com.trevorlitsey.textbustrackerapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    AuthUtils authUtils;

    @Autowired
    GroupService groupService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @GetMapping
    public List<Group> getGroups(@RequestHeader(name = Headers.AUTHORIZATION) String authToken) {
        return groupService.findUserGroups(authUtils.getUserIdFromToken(authToken));
    }

    @PostMapping
    public Group postGroup(@RequestHeader(name = Headers.AUTHORIZATION) String authToken, @RequestBody Group group) {
        return groupService.createGroup(group, authUtils.getUserIdFromToken(authToken));
    }

    @PatchMapping("/{id}")
    public Group patchGroup(@RequestHeader(name = Headers.AUTHORIZATION) String authToken, @PathVariable String id, @RequestBody Group group) {
        return groupService.patchGroup(id, group, authUtils.getUserIdFromToken(authToken));
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@RequestHeader(name = Headers.AUTHORIZATION) String authToken, @PathVariable String id) {
        groupService.deleteGroup(id, authUtils.getUserIdFromToken(authToken));
    }
}
