package com.trevorlitsey.textbustrackerapi.service

import com.trevorlitsey.textbustrackerapi.constants.Collections
import com.trevorlitsey.textbustrackerapi.constants.GroupFields
import com.trevorlitsey.textbustrackerapi.domain.groups.Group
import com.trevorlitsey.textbustrackerapi.domain.groups.Route
import com.trevorlitsey.textbustrackerapi.domain.groups.RouteData
import com.trevorlitsey.textbustrackerapi.repositories.GroupRepository
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import spock.lang.Specification

class GroupServiceSpec extends Specification {
    GroupRepository groupRepository = Mock(GroupRepository)

    MongoOperations mongoOperations = Mock(MongoOperations)

    GroupService groupService = new GroupService(groupRepository: groupRepository, mongoOperations: mongoOperations)

    def 'should find all groups'() {
        setup:
        def userId = '123'
        def groups = List.of(new Group())

        when:
        def result = groupService.findAllGroups(userId)

        then:
        1 * mongoOperations.find(
                Query.query(Criteria.where(GroupFields.USER_ID).is(userId)),
                Group.class,
                Collections.GROUP
        ) >> groups
        result == groups
    }

    def 'should create group'() {
        setup:
        def userId = '123'
        def name = 'foo'
        def routes = List.of(
                new Route(
                        new RouteData('foo', 'bar'),
                        new RouteData('foo', 'bar'),
                        new RouteData('foo', 'bar')
                )
        )
        def keywords = List.of('foo')

        Group insertedGroup

        def expectedGroup = new Group()
        expectedGroup.setUserId(userId)
        expectedGroup.setName(name)
        expectedGroup.setRoutes(routes)
        expectedGroup.setKeywords(keywords)

        when:
        def groupRequest = new Group()
        groupRequest.setName(name)
        groupRequest.setRoutes(routes)
        groupRequest.setKeywords(keywords)
        def result = groupService.createGroup(groupRequest, userId)

        then:
        1 * mongoOperations.findOne(
                Query.query((Criteria.where(GroupFields.NAME).is(name) & GroupFields.USER_ID).is(userId)),
                Group.class,
                Collections.GROUP
        ) >> null
        1 * groupRepository.insert(_) >> {
           insertedGroup = it[0]
        }
        insertedGroup.getName() == name
        insertedGroup.getUserId() == userId
        insertedGroup.getRoutes() == routes
        insertedGroup.getKeywords() == keywords
        result.getName() == name
        result.getUserId() == userId
        result.getRoutes() == routes
        result.getKeywords() == keywords

        when: 'name is missing'
        def missingNameGroupRequest = new Group()
        missingNameGroupRequest.setRoutes(routes)
        missingNameGroupRequest.setKeywords(keywords)
        def missingNameResult = groupService.createGroup(missingNameGroupRequest, userId)

        then: 'should throw error'
        0 * mongoOperations.findOne
        0 * groupRepository.insert
        missingNameResult == null
        def missingNameError = thrown(ResponseStatusException)
        missingNameError.getStatus() == HttpStatus.BAD_REQUEST
        missingNameError.getMessage().contains('Group must have a name')

        when: 'group already exists'
        def groupAlreadyExistsRequest = new Group()
        groupAlreadyExistsRequest.setRoutes(routes)
        groupAlreadyExistsRequest.setName(name)
        groupAlreadyExistsRequest.setKeywords(keywords)
        def groupAlreadyExistsResult = groupService.createGroup(groupAlreadyExistsRequest, userId)

        then: 'should throw error'
        1 * mongoOperations.findOne(
                Query.query((Criteria.where(GroupFields.NAME).is(name) & GroupFields.USER_ID).is(userId)),
                Group.class,
                Collections.GROUP
        ) >> new Group()
        0 * groupRepository.insert
        groupAlreadyExistsResult == null
        def groupAlreadyExistsError = thrown(ResponseStatusException)
        groupAlreadyExistsError.getStatus() == HttpStatus.BAD_REQUEST
        groupAlreadyExistsError.getMessage().contains(String.format("Group with name %s already exists", name))
    }

    def 'should patch group'() {
        setup:
        def userId = '123'
        def groupId = '456'
        def newName = 'foo'
        def newRoutes = List.of(
                new Route(
                        new RouteData('foo', 'bar'),
                        new RouteData('foo', 'bar'),
                        new RouteData('foo', 'bar')
                )
        )
        def newKeywords = List.of('foo')
        def existingName = 'not foo'
        def existingRoutes = List.of()
        def existingKeywords = List.of()
        def existingGroup = new Group()
        existingGroup.setName(existingName)
        existingGroup.setRoutes(existingRoutes)
        existingGroup.setUserId(userId)
        existingGroup.setKeywords(existingKeywords)
        Group updatedGroup
        def groupWithWrongUserId = new Group()
        groupWithWrongUserId.setUserId('not123')

        when: 'should patch name'
        def groupWithName = new Group()
        groupWithName.setName(newName)
        groupService.patchGroup(groupId, groupWithName, userId)

        then:
        1 * groupRepository.findById(groupId) >> Optional.of(existingGroup)
        1 * groupRepository.save(_) >> { updatedGroup = it[0] }
        updatedGroup.getName() == newName
        updatedGroup.getRoutes() == existingRoutes
        updatedGroup.getKeywords() == existingKeywords
        noExceptionThrown()

        when: 'should patch routes'
        def groupWithRoutes = new Group()
        groupWithRoutes.setRoutes(newRoutes)
        groupService.patchGroup(groupId, groupWithRoutes, userId)

        then:
        1 * groupRepository.findById(groupId) >> Optional.of(existingGroup)
        1 * groupRepository.save(_) >> { updatedGroup = it[0] }
        updatedGroup.getName() == existingName
        updatedGroup.getRoutes() == newRoutes
        updatedGroup.getKeywords() == existingKeywords
        noExceptionThrown()

        when: 'should patch keywords'
        def groupWithKeywords = new Group()
        groupWithKeywords.setKeywords(newKeywords)
        groupService.patchGroup(groupId, groupWithKeywords, userId)

        then:
        1 * groupRepository.findById(groupId) >> Optional.of(existingGroup)
        1 * groupRepository.save(_) >> { updatedGroup = it[0] }
        updatedGroup.getName() == existingName
        updatedGroup.getRoutes() == existingRoutes
        updatedGroup.getKeywords() == newKeywords
        noExceptionThrown()

        when: 'group does not exist in mongo'
        groupService.patchGroup(groupId, new Group(), userId)

        then: 'should throw error'
        1 * groupRepository.findById(groupId) >> Optional.ofNullable(null)
        0 * groupRepository.save
        def groupDoesNotExistError = thrown(ResponseStatusException)
        groupDoesNotExistError.getStatus() == HttpStatus.NOT_FOUND
        groupDoesNotExistError.getMessage().contains('Group not found')

        when: 'userId from token does not match mongo'
        groupService.patchGroup(groupId, new Group(), userId)

        then: 'should throw error'
        1 * groupRepository.findById(groupId) >> Optional.of(groupWithWrongUserId)
        0 * groupRepository.save
        def notAuthorizedError = thrown(ResponseStatusException)
        notAuthorizedError.getStatus() == HttpStatus.UNAUTHORIZED
        notAuthorizedError.getMessage().contains(String.format('User not allowed to modify group with id', groupId))
    }

    def 'should delete group'() {
        // TODO
    }
}
