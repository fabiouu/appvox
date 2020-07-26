package com.appvox.core.search

import com.appvox.core.search.domain.request.GooglePlaySearchRequest
import com.appvox.core.search.service.GooglePlaySearchService
import spock.lang.Specification

class GooglePlaySearchServiceSpec extends Specification {

    def "Search Google Play App by name"() {
        given:
            def request = new GooglePlaySearchRequest(appName, language, "", "")

        when:
            def response = GooglePlaySearchService.@INSTANCE.searchAppByName(request)

        then: "we should get a #size reviews from #appId app"
            assert response.apps.size() == expectedSize

        where:
            appName | language ||  expectedSize
            "twitter"   | "en" ||  50
    }
}