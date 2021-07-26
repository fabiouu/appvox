<p align="center">
    <img src="https://user-images.githubusercontent.com/6942446/114973902-f1eb5b00-9eb3-11eb-91e3-814184116b70.png" alt="AppVox" width="300" />
</p>

<p align="center">
    <a href="https://travis-ci.com/fabiouu/AppVox">
        <img src="https://travis-ci.com/fabiouu/AppVox.svg?branch=master" alt="Build Status" />
    </a>
    <a>
        <img src="https://img.shields.io/badge/Kotlin-1.4.21-blue.svg" alt="Coverage" />
    </a>
    <a href="https://codeclimate.com/github/fabiouu/AppVox/maintainability">
        <img src="https://api.codeclimate.com/v1/badges/6f0c3287d031b4f431ea/maintainability" alt="Maintainability" />
    </a>
    <a href="https://codecov.io/gh/fabiouu/AppVox">
        <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
    </a>
    <a href="https://app.fossa.com/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox?ref=badge_shield">
        <img src="https://app.fossa.com/api/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox.svg?type=shield" alt="FOSSA Status"/>
    </a>
</p>

# Overview
Kotlin library to scrape GooglePlay and App Store data. 
Focus on improving your App by listening to your users.
AppVox is a work-in-progress and only supports GooglePlay and AppStore reviews scraping at the moment.
The library offers a convenient way of consuming a [Kotlin Flow](https://kotlinlang.org/docs/flow.html) of reviews with proxy support.

<!-- # Motivation -->

# Table of content
* [Features](#features)
* [Quick start](#quick-start)
* [Usage](#usage)
* [Implementation Details](#implementation-details)
* [License](#license)

# Features
 * Reviews
    * Support Google Play, App Store and Itunes Rss platforms
    * Consume a continuous stream of reviews by leveraging Kotlin Flows. The library handles scraping pagination for you.
    * Filter reviews by language, region, rating, popularity, or user persona
    * Sort reviews by relevance, date or rating
    * Automatically classify users from their reviews to understand more about their behavior

# Quick start
The project is not released to Maven Central yet. Release will come with the project's first stable version.

### App Store
#### Maven
```xml
<dependency>
    <groupId>io.appvox</groupId>
    <artifactId>appvox-appstore</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
#### Gradle
```groovy
dependencies {
    implementation 'io.appvox:appvox-appstore:1.0.0-SNAPSHOT'
}
```

### Google Play
#### Maven
```xml
<dependency>
    <groupId>io.appvox</groupId>
    <artifactId>appvox-googleplay</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
#### Gradle
```groovy
dependencies {
    implementation 'io.appvox:appvox-googleplay:1.0.0-SNAPSHOT'
}
```

### Google Play & App Store
#### Maven
```xml
<dependency>
    <groupId>io.appvox</groupId>
    <artifactId>appvox-all</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
#### Gradle
```groovy
dependencies {
    implementation 'io.appvox:appvox-all:1.0.0-SNAPSHOT'
}
```

We print the 100 most relevant Google Play Reviews of the [Twitter](https://play.google.com/store/apps/details?id=com.twitter.android&hl=en_US&gl=US) App.
This example is using default parameters and no proxy, see the [usage](#usage) section below for more advanced use cases.

```kotlin
import io.appvox.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews { appId = "com.twitter.android" }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

```

# Usage
* [Proxy](#proxy)
* [Reviews](#reviews)
  * [Google Play](#google-play)
  * [Itunes RSS](#itunes-rss)
  * [App Store](#app-store)
  * [Filtering](#filtering)
  * [Classification](#classification)

Only `appvox-core` package is necessary to start using AppVox. 
A variety of filters are available to allow you to focus on the data that matter the most to you.

| Package | Description |
|----------|---------|
| [`appvox-core`](./appvox-core) | Core package contains classes shared by both Google Play and App Store modules |
| [`appvox-googleplay`](./appvox-googleplay) | Core package contains Google Play scraper |
| [`appvox-appstore`](./appvox-appstore) | Core package contains App Store scraper |
| [`appvox-examples`](./appvox-examples) | AppVox usage examples |

The default delay between requests is 500 milliseconds to ensure politeness.
It can be configured in `RequestConfiguration` class.
An `AppVoxException` will be thrown if the configured request delay is inferior to the default delay.

Advanced usage examples can be found in [`appvox-examples`](./appvox-examples) module (pet project).

## Proxy
If you're scraping behind a proxy, you can pass a `java.net.Proxy` object to `RequestConfiguration` accepted by [`GooglePlay.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/GooglePlay.kt), [`AppStore.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/AppStore.kt), [`ItunesRss.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/ItunesRss.kt) constructors.

``` Kotlin
val config = RequestConfiguration(
    delay = 3000,
    proxy = Proxy(Type.HTTP, InetSocketAddress("localhost", 8080))
)

val googlePlay = GooglePlay(config)
val appStore = AppStore(config)
val itunesRss = ItunesRss(config)

// ...
```

## Reviews
In the usage examples below we use [`GooglePlay.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/GooglePlay.kt), [`AppStore.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/AppStore.kt) and [`ItunesRss.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/ItunesRss.kt) to scrape the most recent Twitter app reviews.
The method `take()` will stop the `Flow` of data after scraping 100 reviews. If `take` is not specified,the `Flow` will end when there are no reviews to consume anymore.

For more advanced filtering scenarios, see the [filtering](#filtering) section.Mov

### Google Play
- Language: We specify `ENGLISH_US` parameter since Google Play does not support region filtering (`gl` region parameter is not effective in practice).
- Sorting:  [`GooglePlay.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/GooglePlay.kt) supports sorting reviews by relevance (`RELEVANT`), date (`RECENT`) or rating (`RATING`).
It means fetched reviews are already sorted by `sortType` parameter when scraped from Google Play HTTP endpoints.

```kotlin
import io.appvox.GooglePlay
import io.appvox.review.googleplay.constant.GooglePlayLanguage.ENGLISH_US
import io.appvox.review.googleplay.constant.GooglePlaySortType.RELEVANT
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews {
            appId = "com.twitter.android"
            sortType = RELEVANT
            language = ENGLISH_US
        }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
 ```

### Itunes RSS
- Use Case: Most useful when you want to stay up-to-date on the latest app store reviews and do not intent to fetch the whole app reviews history.
- Region: We specify a `UNITED_STATES` parameter to fetch reviews from users located in the US.
- Sorting: [`ItunesRss.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/ItunesRss.kt) supports sorting reviews by relevance (`RELEVANT`) and date (`RECENT`).

Important:
iTunes RSS feed only returns the 500 most recent or relevant reviews. `take(n)` with `n` > 500 will not yield more results.
Use [App Store](#appstore) scraper below if you intend to fetch more than 500 reviews.
The advantage of Itunes RSS Feed is that it includes metadata such as app version and like count while [App Store](#appstore) reviews do not.

```kotlin
import io.appvox.ItunesRss
import io.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import io.appvox.review.itunesrss.constant.ItunesRssSortType.RECENT
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect

fun main() = runBlocking {
    ItunesRss().reviews {
            appId = appId
            region = UNITED_STATES
            sortType = RECENT
        }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
```

### App Store
- Use Case: Most useful if you intend to fetch all app reviews or more than 500 reviews and do not require sorting by most recent date.
- Language: We specify a `UNITED_STATES` region parameter.
- Sorting: `AppStore` only supports fetching reviews by relevance (`RELEVANT`) and thus, `sortType` parameter is not available.

Important: Reviews are fetched from *apps.apple.com*, a bearer token will automatically be generated before every Flow starts to avoid getting a 401 HTTP response from Apple.
The token is memoized at the beginning of the Flow since its TTL is usually superior to the duration of your Flow.

```kotlin
import io.appvox.AppStore
import io.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppStore()
        .reviews{
            appId = "333903271"
            region = UNITED_STATES
        }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
```

### Filtering
You can take advantage of the flexibility of Kotlin Flows to apply more complex filters on your reviews stream.

For example, we choose to consume only reviews with 100 or more likes:

```kotlin
import io.appvox.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews { appId = "com.twitter.android" }
        .filter { review -> review.likeCount >= 100 }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

```

### Classification
Each review and the user who commented are classified into different categories

#### User Categories
| Name | Availability| Description |
|----------|---------|---------|
|  LOYAL | GooglePlay | Loyal users |


# Implementation Details
##  Architecture
The library is composed of 3 layers:
- Repository (`*Repository.kt` - internal) abstracts network communication with a scraped data source. A repository returns a result entity suffixed with `*Result.kt` (internal).
- Service (`*Service.kt` - internal) contains business logic and may call multiple repositories to process a result entity
- Facade ([`GooglePlay.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/GooglePlay.kt), [`AppStore.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/AppStore.kt), [`ItunesRss.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/ItunesRss.kt) - public) facades expose scraped data in a uniform way to the user. 
A facade returns response entities via a Kotlin coroutine Flow.

## Scrapers
<!-- ### Google Play -->

### Itunes RSS Feed (XML)
The JSON version of the Feed contains no review timestamp which is an essential metadata.
That's why iTunes RSS scraper parses the XML version of the feed.
JAXB included in JRE 8 is used to un-marshal Apple RSS XML into objects.

### App Store
`kotlinx-serialization-json` is used to consume the JSON response returned by `apps.apple.com` scraped's endpoint.
The endpoint returns no more than 10 reviews by request.

<!-- ## Thread Safety -->

<!-- # Memoization -->

<!-- # Retry Policy -->

## Dependencies
AppVox follow a minimal dependency approach.
At the moment, `kotlinx-serialization-json` is not able to parse the unstructured array of data returned by Google Play scraper. Thus, `appvox-googleplay` makes use of `jackson-module-kotlin`.
`appvox-core` and `appvox-appstore` only relies on Kotlin standard library and Kotlinx. 

<!-- # Documentation -->

## Tests
<a href="https://codecov.io/gh/fabiouu/AppVox">
    <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
</a>

The library is covered by a set of Unit and Integration Tests. *WireMock* is used to mock network request responses.

# License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox?ref=badge_large)
