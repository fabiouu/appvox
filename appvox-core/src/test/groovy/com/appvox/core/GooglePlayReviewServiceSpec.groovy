package com.appvox.core

import com.appvox.core.domain.request.review.GooglePlayReviewRequest
import spock.lang.Specification

class GooglePlayReviewServiceSpec extends Specification {

    def "Get google play reviews"() {
        given:
            def request = new GooglePlayReviewRequest(language, sort, size, "", "")

        when:
            def response = GooglePlayReviewService.@INSTANCE.getReviewsByAppId(appId, request)

        then: "we should get a #size reviews from #appId app"
            assert response.reviews.size() == expectedSize

        where:
            appId                   | language | sort  | size   ||  expectedResponseStatus  | expectedSize
            "com.twitter.android"   | "en"     | 1     | 40     ||  200                     | 40
    }
}