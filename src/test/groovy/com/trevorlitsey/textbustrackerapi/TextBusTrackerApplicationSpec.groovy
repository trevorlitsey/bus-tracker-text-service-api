package com.trevorlitsey.textbustrackerapi

import com.trevorlitsey.textbustrackerapi.TextBusTrackerApplication
import spock.lang.Specification

class TextBusTrackerApplicationSpec extends Specification {
    TextBusTrackerApplication textBusTrackerApplication = new TextBusTrackerApplication()

    def 'should run without crashing'() {
        when:
        textBusTrackerApplication.main()

        then:
        noExceptionThrown()
    }
}
