package com.appvox.core.review

import com.appvox.core.review.domain.request.AppStoreReviewRequest
import com.appvox.core.review.service.AppStoreReviewService
import spock.lang.Specification

class AppStoreReviewServiceSpec extends Specification {

    def "Get app store reviews"() {
        given:
            def request = new AppStoreReviewRequest(region, size, "", "")
        when:
            def response = AppStoreReviewService.@INSTANCE.getReviewsByAppId(appId, request)
        then: "we should get a #size reviews from #appId app"
            assert response.data.size() == expectedSize
        where:
            appId       | region    | sort  | size  || expectedResponseStatus   | expectedSize
            "333903271" | "us"      | 1     | 0     || 200                      | 10
    }
}