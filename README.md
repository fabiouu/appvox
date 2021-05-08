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
AppVox supports proxying requests and content translation.

# Table of content
- [Features](#features)
- [Quick start](#quick-start)
- [Usage](#usage)
- [Implementation Details](#implementation-details)
- [License](#license)

# Features
 - Review
    - Enables you to consume a continuous stream of reviews from Google Play or App Store. 
    Reviews can be filtered by region (App Store) or language (Google Play) and sorted by relevance or date.

# Quick start
For now, the project is only available through Jitpack.io repository. Release to Maven Central will come in an ulterior release.

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>dev.fabiou.appvox</groupId>
    <artifactId>appvox-core</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```

### Gradle
```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

```groovy
dependencies {
    implementation 'dev.fabiou.appvox:appvox-core:-SNAPSHOT'
}
```
We print the 100 most relevant Google Play Reviews of the [Twitter](https://play.google.com/store/apps/details?id=com.twitter.android&hl=en_US&gl=US) App.
This example is using default parameters and no proxy, see the [usage](#usage) section below for more advanced use cases.

```kotlin
import dev.fabiou.appvox.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews(appId = "com.twitter.android")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

```

# Usage
Only `appvox-core` package is necessary to start using AppVox.

| Package | Description |
|----------|---------|
| [`appvox-core`](./appvox-core) | Core package contains Google Play and App Store scrapers |
| [`appvox-examples`](./appvox-examples) | AppVox usage examples |

The default delay between requests is 500 milliseconds to ensure politeness. 
It can be configured in `RequestConfiguration` class.
An `AppVoxException` will be thrown if the configured request delay is inferior to the default delay.

Advanced usage examples can be found in [`appvox-examples`](./appvox-examples) module (pet project).

## Proxy
If you're scraping behind a proxy, pass `RequestConfiguration` to `GooglePlay`, `AppStore` or `ItunesRss` constructor.

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
In the usage examples below we use `GooglePlay`, `AppStore` or `ItunesRss` classes to scrape the most recent Twitter app reviews.
The method `take()` will stop the `Flow` of data after scraping 100 reviews. If `take` is not specified,the `Flow` will end when there is not reviews to consume anymore.

For more advanced sorting scenarios, see the [filtering](#filtering) section.

### Google Play
- Language: We specify a `ENGLISH_US` parameter for Google Play since it does not support region. 
`gl` region parameter is present in Google Play scraped HTTP endpoint but not effective in practice. 
- Sorting: `GooglePlay` supports sorting reviews by relevance (`RELEVANT`), date (`RECENT`) or rating (`RATING`) at the scraper level.
It means fetched reviews are already sorted by `sortType` parameter when scraped from Google Play.

```kotlin
import dev.fabiou.appvox.GooglePlay
import dev.fabiou.appvox.review.googleplay.constant.GooglePlayLanguage.ENGLISH_US
import dev.fabiou.appvox.review.googleplay.constant.GooglePlaySortType.RELEVANT
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews(
            appId = "com.twitter.android",
            sortType = RELEVANT,
            language = ENGLISH_US
        )
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
 ```

### Itunes RSS
- Use Case: Most useful when you want to stay up-to-date on the latest app store reviews and do not intent to fetch the whole app reviews history.
- Region: We specify a `UNITED_STATES` parameter to fetch reviews from users located in the US.
- Sorting: `ItunesRss` supports sorting reviews by relevance (`RELEVANT`) and date (`RECENT`).

Important: iTunes RSS feed only returns the 500 most recent or relevant reviews. 
However, it includes metadata such as app version and like count while App Store reviews do not.

If you want to fetch all App Store reviews, use the [App Store](#appstore) scraper instead.

``` Kotlin
import dev.fabiou.appvox.ItunesRss
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import dev.fabiou.appvox.review.itunesrss.constant.ItunesRssSortType.RECENT
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect

fun main() = runBlocking {
    ItunesRss().reviews(
            appId = appId,
            region = UNITED_STATES,
            sortType = RECENT
        )
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
```

### App Store
- Use Case: Most useful if you intend to fetch all app reviews of an app or more than 500 reviews and do not require sorting.
- Language: We specify a `UNITED_STATES` region parameter.
- Sorting: `AppStore` only supports fetching reviews by relevance (`RELEVANT`) and thus, `sortType` parameter is not available.

Important: Reviews are fetched from *apps.apple.com*, a bearer token will automatically be generated before every request to avoid getting a 401 HTTP response.

``` Kotlin
import dev.fabiou.appvox.AppStore
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppStore()
        .reviews(
            appId = "333903271",
            region = UNITED_STATES
        )
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
```

### Filtering
You can take advantage of the flexibility of Kotlin Flows to apply more complex filters on your reviews stream.

For example, we choose to consume only reviews with a user rating equal to 5:

```kotlin
import dev.fabiou.appvox.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay()
        .reviews(appId = "com.twitter.android")
        .filter { review -> review.rating == 5 }
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}

```

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
The JSON version of the Feed contains no review timestamp which is an essential metadata. That's why iTunes RSS scraper parses the XML version of the feed.

### App Store
`apps.apple.com` scraped endpoint returns no more than 10 reviews by request.

<!-- ## Thread Safety -->

## Dependencies
AppVox follow a minimal dependency approach. The only 3rd party dependency of `appvox-core` is `jackson-module-kotlin`.
At the moment, `kotlinx-serialization-json` is not able to parse unstructured array of data returned Google Play scraper.

<!-- # Documentation -->

# Tests
<a href="https://codecov.io/gh/fabiouu/AppVox">
    <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
</a>

The library is covered by a set of Unit and Integration Tests. *WireMock* is used when tests are run locally to mock network request responses and speed-up development iteration.

# License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox?ref=badge_large)
