package com.appvox.api

import com.appvox.api.helper.CursorHelper
import org.springframework.util.Base64Utils
import spock.lang.Specification

import java.text.MessageFormat

class CursorHelperSpec extends Specification {

    def "Decode a Cursor"() {
        given: "an encoded cursor"
            def cursorPattern = "language:{0}|sort:{1}|size:{2}|token:{3}"
            def unencodedCursor = MessageFormat.format(cursorPattern, language, sort, size, token)
            def encodedCursor = Base64Utils.encodeToUrlSafeString(unencodedCursor.getBytes())
        when: "decode a cursor coming from /store/google-play request"

            def parameters = CursorHelper.@Companion.decodeCursorToParameters(encodedCursor)

        then: "we should get a list of decoded parameters"
            parameters["language"] == language
            parameters["sort"] == sort
            parameters["size"] == size
            parameters["token"] == token

        where:
            language  | sort  | size  | token
            "en"      | "1"   | "50"    | "dummy_token"
    }

    def "Encode a Cursor"() {
        given: "a list of parameters to encode"
            def parameters =  ["language":language, "sort":sort,"size":size,"token":token]
        when: "encode a cursor with the list of parameters"
            def cursor = CursorHelper.@Companion.encodeParametersToCursor(parameters)
        then: "An encoded base64 cursor"
            cursor == expectedCursor

        where:
            language  | sort  | size      | token         || expectedCursor
            "en"      | "1"   | "40"      | "CqYBCqMBKn4KOfc7ms1u_rjuD5ielp7Fz8_Pz8_PxpnLx82ZycrOm8XOxYmSxc3IysfIxsjJx8fHys_HycbPxs___hAoIW2BNraj5FHTMfpVIEL98xFFMTuKKaRqousoMblJNZOlWEGROfARRwGRMmXEUABaCwlC7l5k2IqOjhADYNT7oWMyIQofCh1hbmRyb2lkX2hlbHBmdWxuZXNzX3FzY29yZV92Mg" || "bGFuZ3VhZ2U6ZW58c29ydDoxfHNpemU6NDB8dG9rZW46Q3FZQkNxTUJLbjRLT2ZjN21zMXVfcmp1RDVpZWxwN0Z6OF9QejhfUHhwbkx4ODJaeWNyT204WE94WW1TeGMzSXlzZkl4c2pKeDhmSHlzX0h5Y2JQeHNfX19oQW9JVzJCTnJhajVGSFRNZnBWSUVMOTh4RkZNVHVLS2FScW91c29NYmxKTlpPbFdFR1JPZkFSUndHUk1tWEVVQUJhQ3dsQzdsNWsySXFPamhBRFlOVDdvV015SVFvZkNoMWhibVJ5YjJsa1gyaGxiSEJtZFd4dVpYTnpYM0Z6WTI5eVpWOTJNZw=="
    }
}