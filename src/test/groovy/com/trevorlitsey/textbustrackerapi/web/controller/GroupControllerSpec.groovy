package com.trevorlitsey.textbustrackerapi.web.controller

import com.trevorlitsey.textbustrackerapi.domain.groups.Group
import com.trevorlitsey.textbustrackerapi.service.GroupService
import com.trevorlitsey.textbustrackerapi.service.UserService
import com.trevorlitsey.textbustrackerapi.utils.AuthUtils
import com.trevorlitsey.textbustrackerapi.utils.JwtUtil
import spock.lang.Specification

class GroupControllerSpec extends Specification {
    AuthUtils authUtils = Mock(AuthUtils)

    GroupService groupService = Mock(GroupService)

    JwtUtil jwtUtil = Mock(JwtUtil)

    UserService userService = Mock(UserService)

    GroupController groupController = new GroupController(authUtils: authUtils, groupService: groupService, jwtUtil: jwtUtil, userService: userService)

    def 'should get groups'() {
        setup:
        def authToken = 'token123'
        def userId = '456'
        def groups = List.of(Group.builder().build())

        when:
        def result = groupController.getGroups(authToken)

        then:
        1 * authUtils.getUserIdFromToken(authToken) >> userId
        1 * groupService.findAllGroups(userId) >> groups
        result == groups
    }

    def 'should post group'() {
        setup:
        def authToken = 'token123'
        def userId = '456'
        def group = Group.builder().build()

        when:
        def result = groupController.postGroup(authToken, group)

        then:
        1 * authUtils.getUserIdFromToken(authToken) >> userId
        1 * groupService.createGroup(group, userId) >> group
        result == group
    }

    def 'should patch group'() {
        setup:
        def authToken = 'token123'
        def userId = '456'
        def groupId = '789'
        def group = Group.builder().build()

        when:
        def result = groupController.patchGroup(authToken, groupId, group)

        then:
        1 * authUtils.getUserIdFromToken(authToken) >> userId
        1 * groupService.patchGroup(groupId, group, userId) >> group
        result == group
    }

    def 'should delete group'() {
        setup:
        def authToken = 'token123'
        def userId = '456'
        def groupId = '789'

        when:
        def result = groupController.deleteGroup(authToken, groupId)

        then:
        1 * authUtils.getUserIdFromToken(authToken) >> userId
        1 * groupService.deleteGroup(groupId, userId)
        result == null
    }

}
