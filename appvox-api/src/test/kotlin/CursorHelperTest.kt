package com.appvox.core.review.helper

import com.appvox.api.helper.CursorHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.nio.charset.Charset
import java.util.*

class CursorHelperTest {

    @ParameterizedTest
    @CsvSource(
        "en, 1, 50, dummy_token"
    )
    fun `Decode a Cursor`(language : String, sort : String, size : String, token : String) {
        val unencodedCursor = "language:$language|sort:$sort|size:$size|token:$token"
        val encodedCursor: String = Base64.getEncoder().encodeToString(
            unencodedCursor.toByteArray(Charset.defaultCharset()))

        val parameters = CursorHelper.decodeCursorToParameters(encodedCursor)

        Assertions.assertEquals( parameters["language"], language)
        Assertions.assertEquals( parameters["sort"], sort)
        Assertions.assertEquals( parameters["size"], size)
        Assertions.assertEquals( parameters["token"], token)
    }

    @ParameterizedTest
    @CsvSource(
        "en, 1, 40, CqYBCqMBKn4KOfc7ms1u_rjuD5ielp7Fz8_Pz8_PxpnLx82ZycrOm8XOxYmSxc3IysfIxsjJx8fHys_HycbPxs___hAoIW2BNraj5FHTMfpVIEL98xFFMTuKKaRqousoMblJNZOlWEGROfARRwGRMmXEUABaCwlC7l5k2IqOjhADYNT7oWMyIQofCh1hbmRyb2lkX2hlbHBmdWxuZXNzX3FzY29yZV92Mg, bGFuZ3VhZ2U6ZW58c29ydDoxfHNpemU6NDB8dG9rZW46Q3FZQkNxTUJLbjRLT2ZjN21zMXVfcmp1RDVpZWxwN0Z6OF9QejhfUHhwbkx4ODJaeWNyT204WE94WW1TeGMzSXlzZkl4c2pKeDhmSHlzX0h5Y2JQeHNfX19oQW9JVzJCTnJhajVGSFRNZnBWSUVMOTh4RkZNVHVLS2FScW91c29NYmxKTlpPbFdFR1JPZkFSUndHUk1tWEVVQUJhQ3dsQzdsNWsySXFPamhBRFlOVDdvV015SVFvZkNoMWhibVJ5YjJsa1gyaGxiSEJtZFd4dVpYTnpYM0Z6WTI5eVpWOTJNZw=="
    )
    fun `Encode a Cursor`(language : String, sort : String, size : String, token : String, expectedCursor : String) {
        val parameters =  mapOf(
            "language" to language,
            "sort" to sort,
            "size" to size,
            "token" to token
        )
        val cursor = CursorHelper.encodeParametersToCursor(parameters)

        Assertions.assertEquals(cursor, expectedCursor)
    }
}