package io.appvox.appstore.review.domain

import kotlinx.serialization.Serializable

@Serializable
internal class AppStoreReviewJsonResultTop(
    val feed: AppStoreReviewFeed
)

@Serializable
internal class AppStoreReviewFeed(
    val entry: List<AppStoreReviewEntry>
)

@Serializable
internal class AppStoreReviewEntry(

//    val uri: String? = null

//    @XmlElement(required = true)
//    val id: String? = null
//
//    @XmlElement(required = true)
//    val title: String? = null
//
//    @XmlElement(required = true)
//    @XmlSchemaType(name = "dateTime")
//    val updated: XMLGregorianCalendar? = null
//
//    @XmlElement(required = true)
//    val link: List<Link>? = null
//
//    @XmlElement(required = true)
//    @XmlSchemaType(name = "anyURI")
//    val icon: String? = null
//
//    @XmlElement(required = true)
    val author: Author
//
//    @XmlElement(required = true)
//    val rights: String? = null
//
//    @XmlElement(required = true)
//    val entry: List<Entry>? = null
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    @XmlType(
//        name = "", propOrder = [
//        "updated", "id", "title", "content", "voteSum", "voteCount", "rating", "version", "author", "link"]
//    )
//    @XmlRootElement(name = "entry")
//    internal class Entry {
//        @XmlElement(required = true)
//        @XmlSchemaType(name = "dateTime")
//        val updated: XMLGregorianCalendar? = null
//
//        @XmlElement(required = true)
//        val id: String? = null
//
//        @XmlElement(required = true)
//        val title: String? = null
//
//        @XmlElement(required = true)
//        val content: List<Content>? = null
//
//        @XmlElement(required = true, name = "im:voteSum")
//        val voteSum: Int = 0
//
//        @XmlElement(required = true, name = "im:voteCount")
//        val voteCount: Int = 0
//
//        @XmlElement(required = true, name = "im:rating")
//        val rating: Int = 0
//
//        @XmlElement(required = true, name = "im:version")
//        val version: String? = null
//
//        @XmlElement(required = true)
//        val author: Author? = null
//
//        @XmlElement(required = true)
//        val link: Link? = null
//
//        @XmlRootElement(name = "content")
//        @XmlAccessorType(XmlAccessType.FIELD)
//        internal class Content {
//            @XmlValue
//            val text: String? = null
//
//            @XmlAttribute
//            val type: String? = null
//        }
//    }
//
//    @XmlAccessorType(XmlAccessType.FIELD)
//    @XmlType(name = "", propOrder = ["value"])
//    @XmlRootElement(name = "link")
//    internal class Link {
//        @XmlValue
//        val value: String? = null
//
//        @XmlAttribute(name = "rel")
//        val rel: String? = null
//
//        @XmlAttribute(name = "href")
//        val href: String? = null
//
//        @XmlAttribute(name = "type")
//        val type: String? = null
//    }
//
)

@Serializable
internal data class Author(val name: Label, val uri: Label)

@Serializable
internal data class Label(val label: String)

