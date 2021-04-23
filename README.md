<p align="center">
    <img src="https://user-images.githubusercontent.com/6942446/114973902-f1eb5b00-9eb3-11eb-91e3-814184116b70.png" alt="AppVox" width="300" />
</p>

<p align="center">
    <a href="https://travis-ci.com/fabiouu/AppVox">
        <img src="https://travis-ci.com/fabiouu/AppVox.svg?branch=master" alt="Build Status" />
    </a>
    <a href="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-1.4.21-blue.svg" alt="Coverage" />
    </a>
    <a href="https://codeclimate.com/github/fabiouu/AppVox/maintainability">
        <img src="https://api.codeclimate.com/v1/badges/6f0c3287d031b4f431ea/maintainability" alt="Maintainability" />
    </a>
    <a href="https://codecov.io/gh/fabiouu/AppVox">
        <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
    </a>
    <a href="https://app.fossa.com/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox?ref=badge_shield" alt="FOSSA Status">
        <img src="https://app.fossa.com/api/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox.svg?type=shield"/>
    </a>
</p>

# Overview
Kotlin library to scrape GooglePlay and App Store data. Forget about scraping details, focus on your users!
AppVox supports proxying requests and content translation.

# Features
 - Review
    - The Review module Enables you to consume a continuous stream of reviews, 

# Installation
Only `appvox-core` package is necessary to start using AppVox

| Package | Description |
|----------|---------|
| [`appvox-core`](./appvox-core) | Core package contains Google Play and App Store scrapers |
| [`appvox-examples`](./appvox-examples) | AppVox usage examples |

### Maven
```xml
```

### Gradle
```groovy
```

# Usage
AppVox scraper is polite by default. The default delay between requests is 500 milliseconds.
An `AppVoxException` will be thrown if user-defined request delay is inferior to the default delay.

Complete usage examples can be found in [`appvox-examples`](./appvox-examples) module.
## Proxy
If you're scraping behind a proxy, pass `RequestConfiguration` to `GooglePlay` or `AppStore` constructor.
``` Kotlin
val config = RequestConfiguration(
    delay = 3000,
    proxy = ProxyConfiguration(
        host = "127.0.0.1",
        port = 8080
    )
)

val googlePlay = GooglePlay(config)
val appStore = AppStore(config)

// ...
```

## Reviews
Use `GooglePlay` or `AppStore` class with default parameters to scrape the most recent English Twitter app reviews.
The method `take()` will stop the `Flow` of data after scraping 100 reviews (optional).
### Google Play
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
        ).collect { review ->
            println(review.toString())
        }
}
 ```

### App Store
``` Kotlin
import dev.fabiou.appvox.AppStore
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreSortType.RELEVANT
import dev.fabiou.appvox.review.itunesrss.constant.AppStoreRegion.UNITED_STATES
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppStore()
        .reviews(
            appId = "333903271",
            sortType = RELEVANT,
            region = UNITED_STATES
        ).collect { review ->
            println(review.toString())
        }
}
```

#### Internals
App Store scraper implementation requests reviews from 2 different data sources depending on `AppStoreSortType` parameter value
- apps.apple.com: to return the whole review history of an app sorted by most relevant.
Moreover, the scrapped endpoint return no more than 10 reviews by request (fixed size by Apple).
This approach is limited if your goal is to stay up-to-date on the latest user reviews and do not include user app version.
That's why the tool offer a second way of getting the most recent App Store reviews
- Itunes RSS XML Feed. The JSON version of the Feed contains no review timestamp.
The RSS Feed returns the 500 most recent reviews at most and include more metadata such as app version and like count while App Store reviews do not.

Using both implementation is transparent for the user, just specify `AppStoreSortType` to MOST_RECENT or MOST_RELEVANT to switch between the two methods

# About AppVox
## Motivation

##  Architecture
The library is composed of mainly 3 layers:
- Repository (`*Repository.kt` - internal): A repository abstracts network communication with a scraped data source. A repository returns a result entity suffixed with `*Result.kt` (internal).
- Service (`*Service.kt` - internal): A service contains business logic and may have to call multiple repositories to process a result entity
- Facade ([`GooglePlay.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/core/GooglePlay.kt), [`AppStore.kt`](./appvox-core/src/main/kotlin/dev/fabiou/appvox/core/AppStore.kt) - public): A facade exposes scraped data in a uniform way to the user. A facade returns response entities via a Kotlin coroutine Flow.

## Documentation

## Testing
<a href="https://codecov.io/gh/fabiouu/AppVox">
    <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
</a>

The library is covered by a set of Unit and Integration Tests. *WireMock* is used when tests are run locally to mock network request responses and speed-up development iteration.
To deactivate HTTP requests mocking...

## Thread Safety
AppVox uses Kotlin Flows

## Dependencies
AppVox follow a minimal dependency approach. The only 3rd party dependency of `appvox-core` is `jackson-module-kotlin`.
At the moment, `kotlinx-serialization-json` is not able to parse unstructured array of data returned Google Play scraper.

## Code Analysis
The project is covered by Detekt static code analysis.

# Roadmap
Roadmap and tasks in-progress of the project can be found in the upper "Projects" GitHub section

# Contributions

# License
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2Ffabiouu%2Fappvox?ref=badge_large)
