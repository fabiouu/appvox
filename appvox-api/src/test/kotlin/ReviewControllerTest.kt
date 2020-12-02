import com.appvox.api.AppVoxApplication
import com.appvox.core.review.domain.response.GooglePlayReviewResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(AppVoxApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerTest(
    @Autowired
    private val restTemplate: TestRestTemplate) {

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, us, en, 1, 40, 200, 40"
    )
    fun `Get app reviews from Google Play with no cursor`(
        appId: String, region: String, language: String, sort: Int, size: Int,
        expectedResponseStatus: Int, expectedSize: Int) {
        val url = "/store/google-play/app/$appId/reviews?language=$language&sortType=$sort&size=$size"

        val response = restTemplate.getForEntity(url, GooglePlayReviewResponse::class.java)

        Assertions.assertEquals(response.statusCodeValue, expectedResponseStatus)
        Assertions.assertEquals(response.body?.reviews?.size, expectedSize)
    }

    @ParameterizedTest
    @CsvSource(
        "com.twitter.android, us, en, 1, 40, 200, 40"
    )
    fun `Get app reviews from Google Play with cursor`(
        appId: String, region: String, language: String, sort: Int, size: Int,
        expectedResponseStatus: Int, expectedSize: Int) {
//        val url = "/store/google-play/app/$appId/reviews?language=$language&sortType=$sort&size=$size"
//
//        val initialResponse = restTemplate.getForEntity(url, GooglePlayReviewResponse::class.java)
//        val nextCursor = initialResponse.body?.nextCursor
//
//        val urlWithCursor = "/store/google-play/app/$appId/reviews?cursor=$nextCursor"
//        val response = restTemplate.getForEntity(urlWithCursor, GooglePlayReviewResponse::class.java)
//
//        Assertions.assertEquals(response.statusCodeValue, expectedResponseStatus)
//        Assertions.assertEquals(response.body?.reviews?.size, expectedSize)
    }

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 0, 200, 10"
    )
    fun `Get app reviews from App Store with no cursor`(
        appId: String, region: String,sort: Int, page : Int, expectedResponseStatus: Int, expectedSize: Int) {
        val url = "/store/app-store/app/$appId/reviews?region=$region&page=$page"

        val response = restTemplate.getForEntity(url, GooglePlayReviewResponse::class.java)

        Assertions.assertEquals(response.statusCodeValue, expectedResponseStatus)
        Assertions.assertEquals(response.body?.reviews?.size, expectedSize)
    }

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 0, 200, 10"
    )
    fun `Get app reviews from App Store with cursor`(
        appId: String, region: String, sort: Int, page : Int,
        expectedResponseStatus: Int, expectedSize: Int) {
//        val url = "/store/app-store/app/$appId/reviews?region=$region&page=$page"
//
//        val initialResponse = restTemplate.getForEntity(url, GooglePlayReviewResponse::class.java)
//        val nextCursor = initialResponse.body?.nextCursor
//
//        val urlWithCursor = "/store/app-store/app/$appId/reviews?cursor=$nextCursor"
//        val response = restTemplate.getForEntity(urlWithCursor, GooglePlayReviewResponse::class.java)
//
//        Assertions.assertEquals(response.statusCodeValue, expectedResponseStatus)
//        Assertions.assertEquals(response.body?.reviews?.size, expectedSize)
    }
}