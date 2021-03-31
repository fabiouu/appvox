package dev.fabiou.appvox.core.appstore.review.domain

import javax.xml.bind.annotation.*
import javax.xml.datatype.XMLGregorianCalendar

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["id", "title", "updated", "link", "icon", "author", "rights", "entry"])
@XmlRootElement(name = "feed")
internal class ItunesRssReviewResult {

    @XmlAttribute(name = "xmlns")
    var uri: String? = null

    @XmlElement(required = true)
    var id: String? = null

    @XmlElement(required = true)
    var title: String? = null

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    var updated: XMLGregorianCalendar? = null

    @XmlElement(required = true)
    var link: List<Link>? = null

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    var icon: String? = null

    @XmlElement(required = true)
    var author: Author? = null

    @XmlElement(required = true)
    var rights: String? = null

    @XmlElement(required = true)
    var entry: List<Entry>? = null

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = [
        "updated", "id", "title", "content", "voteSum", "voteCount", "rating", "version", "author", "link"])
    @XmlRootElement(name = "entry")
    internal class Entry {
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        var updated: XMLGregorianCalendar? = null

        @XmlElement(required = true)
        var id: String? = null

        @XmlElement(required = true)
        var title: String? = null

        @XmlElement(required = true)
        var content: List<Content>?= null

        @XmlElement(required = true, name = "im:voteSum")
        var voteSum: Int? = 0

        @XmlElement(required = true, name = "im:voteCount")
        var voteCount: Int? = 0

        @XmlElement(required = true, name = "im:rating")
        var rating: Int? = 0

        @XmlElement(required = true, name = "im:version")
        var version: String? = null

        @XmlElement(required = true)
        var author: Author? = null

        @XmlElement(required = true)
        var link: Link? = null

        @XmlRootElement(name = "content")
        @XmlAccessorType(XmlAccessType.FIELD)
        internal class Content {
            @XmlValue
            var content: String? = null

            @XmlAttribute
            var type: String? = null
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = ["value"])
    @XmlRootElement(name = "link")
    internal class Link {
        @XmlValue
        var value: String? = null

        @XmlAttribute(name = "rel")
        var rel: String? = null

        @XmlAttribute(name = "href")
        var href: String? = null

        @XmlAttribute(name = "type")
        var type: String? = null
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = ["name", "uri"])
    @XmlRootElement(name = "author")
    internal class Author {
        @XmlElement(required = true)
        var name: String? = null

        @XmlElement(required = true)
        var uri: String? = null
    }
}