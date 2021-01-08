package com.trevorlitsey.textbustrackerapi.web.controller

import spock.lang.Specification

class HealthControllerSpec extends Specification {
    HealthController healthController = new HealthController()

    def 'should return health check'() {
        when:
        def result = healthController.getHealth()

        then:
        result == 'ok'
    }
}
