package com.appvox.core.review

import com.appvox.core.config.Configuration
import com.appvox.core.review.domain.request.GooglePlayReviewRequest
import com.appvox.core.review.service.GooglePlayReviewService
import spock.lang.Specification

class GooglePlayReviewServiceSpec extends Specification {

//    def "Get google play reviews"() {
//        given:
//            def request = new GooglePlayReviewRequest(language, sort, size, "", "")
//
//        when:
//            def response = GooglePlayReviewService.@Companion.getInstance().getReviewsByAppId(appId, request)
//
//        then: "we should get a #size reviews from #appId app"
//            assert response.reviews.size() == expectedSize
//
//        where:
//            appId                   | language | sort  | size   ||  expectedResponseStatus  | expectedSize
//            "com.twitter.android"   | "en"     | 1     | 40     ||  200                     | 40
//    }

    def "Get google play reviews through a proxy"() {
        given:
        def request = new GooglePlayReviewRequest(language, sort, size, "", "")
//        def config = new Configuration("127.0.0.1", 1080, "", "")
        def config = Configuration.@Builder.proxyHost("127.0.0.1").proxyPort(1080).build()
//        def config = Configuration.Builder().proxyHost("X").build()

        when:
        def response = GooglePlayReviewService.@Companion.getInstance(config).getReviewsByAppId(appId, request)

        then: "we should get a #size reviews from #appId app"
        assert response.reviews.size() == expectedSize

        where:
        appId                   | language | sort  | size   ||  expectedResponseStatus  | expectedSize
        "com.twitter.android"   | "en"     | 1     | 40     ||  200                     | 40
    }
}