package dev.fabiou.appvox.core.review.service

import dev.fabiou.appvox.core.BaseStoreServiceTest
import dev.fabiou.appvox.core.configuration.Configuration
import dev.fabiou.appvox.core.review.constant.AppStoreSortType
import dev.fabiou.appvox.core.review.domain.request.AppStoreReviewRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AppStoreRecentReviewServiceTest : BaseStoreServiceTest() {

    private var service = AppStoreRecentReviewService(Configuration(requestDelay = 3000L))

    @ParameterizedTest
    @CsvSource(
        "333903271, us, 1, 1"
    )
    fun `Get most recent App Store reviews from itunes RSS Feed`(appId: String, region: String, requestedSize: Int, pageNo: Int) {

        stubHttpUrl(AppStoreRecentReviewService.RSS_REQUEST_URL_PATH.format(region, pageNo, appId), "<feed xmlns:im=\"http://itunes.apple.com/rss\" xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en\">\n" +
                "<id>https://17.124.160.227/us/rss/customerreviews/page=1/id=333903271/sortby=mostrecent/xml?urlDesc=/customerreviews/id=333903271/mostrecent/xml</id>\n" +
                "<title>iTunes Store: Customer Reviews</title>\n" +
                "<updated>2021-02-19T07:01:15-07:00</updated>\n" +
                "<link rel=\"alternate\" type=\"text/html\" href=\"https://music.apple.com/WebObjects/MZStore.woa/wa/viewGrouping?cc=us&id=1\"/>\n" +
                "<link rel=\"self\" href=\"https://17.124.160.227/us/rss/customerreviews/page=1/id=333903271/sortby=mostrecent/xml?urlDesc=/customerreviews/id=333903271/mostrecent/xml\"/>\n" +
                "<link rel=\"first\" href=\"https://itunes.apple.com/us/rss/customerreviews/page=1/id=333903271/sortby=mostrecent/xml?urlDesc=/customerreviews/page=1/id=333903271/sortby=mostrecent/xml\"/>\n" +
                "<link rel=\"last\" href=\"https://itunes.apple.com/us/rss/customerreviews/page=10/id=333903271/sortby=mostrecent/xml?urlDesc=/customerreviews/page=1/id=333903271/sortby=mostrecent/xml\"/>\n" +
                "<link rel=\"previous\" href=\"https://itunes.apple.com/us/rss/customerreviews/page=1/id=333903271/sortby=mostrecent/xml?urlDesc=/customerreviews/page=1/id=333903271/sortby=mostrecent/xml\"/>\n" +
                "<link rel=\"next\" href=\"https://itunes.apple.com/us/rss/customerreviews/page=2/id=333903271/sortby=mostrecent/xml?urlDesc=/customerreviews/page=1/id=333903271/sortby=mostrecent/xml\"/>\n" +
                "<icon>http://itunes.apple.com/favicon.ico</icon>\n" +
                "<author>\n" +
                "<name>iTunes Store</name>\n" +
                "<uri>http://www.apple.com/itunes/</uri>\n" +
                "</author>\n" +
                "<rights>Copyright 2008 Apple Inc.</rights>\n" +
                "<entry>\n" +
                "<updated>2021-02-18T02:49:45-07:00</updated>\n" +
                "<id>7008886870</id>\n" +
                "<title>Twitter</title>\n" +
                "<content type=\"text\">Cool way to show feelings and support other people</content>\n" +
                "<im:contentType term=\"Application\" label=\"Application\"/>\n" +
                "<im:voteSum>0</im:voteSum>\n" +
                "<im:voteCount>0</im:voteCount>\n" +
                "<im:rating>5</im:rating>\n" +
                "<im:version>8.53</im:version>\n" +
                "<author>\n" +
                "<name>beastmatin</name>\n" +
                "<uri>https://itunes.apple.com/us/reviews/id1196030906</uri>\n" +
                "</author>\n" +
                "<link rel=\"related\" href=\"https://itunes.apple.com/us/review?id=333903271&type=Purple%20Software\"/>\n" +
                "<content type=\"html\"><table border=\"0\" width=\"100%\"> <tr> <td> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tr valign=\"top\" align=\"left\"> <td width=\"100%\"> <b><a href=\"https://apps.apple.com/us/app/twitter/id333903271?uo=2\">Twitter</a></b><br/> <font size=\"2\" face=\"Helvetica,Arial,Geneva,Swiss,SunSans-Regular\"> </font> </td> </tr> </table> </td> </tr> <tr> <td> <font size=\"2\" face=\"Helvetica,Arial,Geneva,Swiss,SunSans-Regular\"><br/>Cool way to show feelings and support other people</font><br/> </td> </tr> </table> </content>\n" +
                "</entry>\n" +
                "</feed>")

        val request = AppStoreReviewRequest(
            region = region,
            sortType = AppStoreSortType.RECENT,
            pageNo = 1
        )

        val response = service.getReviewsByAppId(appId, request)

        Assertions.assertEquals(requestedSize, response.entry?.size)
    }
}