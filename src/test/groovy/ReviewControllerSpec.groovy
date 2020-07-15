import com.appvox.appvox.AppVoxApplication
import com.appvox.appvox.domain.response.ReviewsResponse
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import spock.lang.Specification
import spock.lang.Unroll

import java.text.MessageFormat

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = [AppVoxApplication], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    def "Get app reviews from Google Play with no token"() {
        when: "we send a request to Google Play for reviews"
            String urlPattern = "/store/google-play/app/{0}/reviews?language={1}&sortType={2}&size={3}"
            String url = MessageFormat.format(urlPattern, appId, language, sort, size);
            def resp = restTemplate.getForEntity(url, ReviewsResponse)

        then: "we should get a #size reviews from #appId app"
            assert resp.status == responseStatus
            assert resp.body.reviews.size == reviewCount

        where:
            appId                   | region | language | sort  | size   ||  responseStatus  | reviewCount
            "com.twitter.android"   | "us"   | "en"     | 1     | 40     ||  200             | 40
    }

    def "Get app reviews from App Store"() {
        when: "we send a request to App Store for reviews"
            String urlPattern = "/store/app-store/app/{0}/reviews?region={1}&page={2}"
            String url = MessageFormat.format(urlPattern, appId, region, page);
            def resp = restTemplate.getForEntity(url, ReviewsResponse)

        then: "we should get a #size reviews from #appId app"
            assert resp.status == responseStatus
            assert resp.body.reviews.size == reviewCount
        where:
            appId       | region    | language  | sort  | page  || responseStatus   | reviewCount
            "333903271" | "us"      | "en"      | 1     | 0     || 200              | 10
    }
}