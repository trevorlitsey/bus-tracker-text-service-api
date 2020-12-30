package com.trevorlitsey.poolpractice.web.controller;

import com.trevorlitsey.poolpractice.domain.Group;
import com.trevorlitsey.poolpractice.service.GroupService;
import com.trevorlitsey.poolpractice.service.UserService;
import com.trevorlitsey.poolpractice.utils.AuthUtils;
import com.trevorlitsey.poolpractice.utils.JwtUtil;
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
    public List<com.trevorlitsey.poolpractice.domain.Group> getGroups(@RequestHeader(name = "Authorization") String authToken) {
        return groupService.findAllGroups(authUtils.getUserIdFromToken(authToken));
    }

    @PostMapping
    public com.trevorlitsey.poolpractice.domain.Group postGroup(@RequestHeader(name = "Authorization") String authToken, @RequestBody Group group) {
        return groupService.createGroup(group, authUtils.getUserIdFromToken(authToken));
    }

    @PatchMapping("/{id}")
    public com.trevorlitsey.poolpractice.domain.Group putGroup(@RequestHeader(name = "Authorization") String authToken, @PathVariable String id, @RequestBody Group group) {
        return groupService.patchRoutine(id, group, authUtils.getUserIdFromToken(authToken));
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@RequestHeader(name = "Authorization") String authToken, @PathVariable String id) {
        groupService.deleteGroup(id, authUtils.getUserIdFromToken(authToken));
    }
}
