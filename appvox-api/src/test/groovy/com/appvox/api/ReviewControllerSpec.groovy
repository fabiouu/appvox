package com.appvox.api

import com.appvox.core.domain.response.review.ReviewsResponse
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import spock.lang.Specification

import java.text.MessageFormat

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = [AppVoxApplication], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    def "Get app reviews from Google Play with no cursor"() {
        when: "we send a request to Google Play for reviews"
            String urlPattern = "/store/google-play/app/{0}/reviews?language={1}&sortType={2}&size={3}"
            String url = MessageFormat.format(urlPattern, appId, language, sort, size)
            def response = restTemplate.getForEntity(url, ReviewsResponse)

        then: "we should get #size reviews from #appId app"
            assert response.status == expectedResponseStatus
            assert response.body.reviews.size() == expectedSize

        where:
            appId                   | region | language | sort  | size   ||  expectedResponseStatus  | expectedSize
            "com.twitter.android"   | "us"   | "en"     | 1     | 40     ||  200                     | 40
    }

    def "Get app reviews from Google Play with cursor"() {
        given: "an initial request has been made to get the next cursor"
            String initialUrlPattern = "/store/google-play/app/{0}/reviews?language={1}&sortType={2}&size={3}"
            String initialUrl = MessageFormat.format(initialUrlPattern, appId, language, sort, size)
            def initialResponse = restTemplate.getForEntity(initialUrl, ReviewsResponse)
            def nextCursor = initialResponse.body.nextCursor

        when: "we send a request to Google Play for reviews with a next cursor"
            String urlPattern = "/store/google-play/app/{0}/reviews?cursor={1}"
            String url = MessageFormat.format(urlPattern, appId, nextCursor)
            def response = restTemplate.getForEntity(url, ReviewsResponse)

        then: "we should get a #size reviews from #appId app"
            assert response.status == expectedResponseStatus
            assert response.body.reviews.size() == expectedSize

        where:
            appId                   | region | language | sort  | size  ||  expectedResponseStatus  | expectedSize
            "com.twitter.android"   | "us"   | "en"     | 1     | 40    ||  200                     | 40
    }

    def "Get app reviews from App Store"() {
        when: "we send a request to App Store for reviews"
            String urlPattern = "/store/app-store/app/{0}/reviews?region={1}&page={2}"
            String url = MessageFormat.format(urlPattern, appId, region, page)
            def response = restTemplate.getForEntity(url, ReviewsResponse)

        then: "we should get a #size reviews from #appId app"
            assert response.status == expectedResponseStatus
            assert response.body.reviews.size() == expectedSize
        where:
            appId       | region    | language  | sort  | page  || expectedResponseStatus   | expectedSize
            "333903271" | "us"      | "en"      | 1     | 0     || 200                      | 10
    }

    def "Get app reviews from App Store with cursor"() {
        given: "an initial request has been made to get the next cursor"
            String initialUrlPattern = "/store/app-store/app/{0}/reviews?region={1}&page={2}"
            String initialUrl = MessageFormat.format(initialUrlPattern, appId, region, page)
            def initialResponse = restTemplate.getForEntity(initialUrl, ReviewsResponse)
            def nextCursor = initialResponse.body.nextCursor

        when: "we send a request to Google Play for reviews with a next cursor"
            String urlPattern = "/store/app-store/app/{0}/reviews?cursor={1}"
            String url = MessageFormat.format(urlPattern, appId, nextCursor)
            def response = restTemplate.getForEntity(url, ReviewsResponse)

        then: "we should get a #size reviews from #appId app"
            assert response.status == expectedResponseStatus
            assert response.body.reviews.size() == expectedSize

        where:
            appId       | region    | language  | sort  | page  || expectedResponseStatus   | expectedSize
            "333903271" | "us"      | "en"      | 1     | 0     || 200                      | 10
    }
}