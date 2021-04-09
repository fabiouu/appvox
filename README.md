<h1 align="center">
  AppVox
</h1>

<p align="center">
    <a href="https://travis-ci.com/fabiouu/AppVox">
        <img src="https://travis-ci.com/fabiouu/AppVox.svg?branch=master" alt="Build Status" />
    </a>
    <a href="https://kotlinlang.org">
        <img src="https://maven-badges.herokuapp.com/maven-central/dev.fabiou.appvox/appvox-parent/badge.svg" alt="Coverage" />
    </a>
    <a href="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-1.4.32-blue.svg" alt="Coverage" />
    </a>
    <a href="https://codeclimate.com/github/fabiouu/AppVox/maintainability">
        <img src="https://api.codeclimate.com/v1/badges/6f0c3287d031b4f431ea/maintainability" alt="Maintainability" />
    </a>
    <a href="https://codecov.io/gh/fabiouu/AppVox">
        <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
    </a>
</p>

<h3 align="center">
  Capture the voice of your App users
</h3>

## Overview
AppVox is a high-level library to extract application data from Google Play and App Store via a uniform interface.

For now, AppVox offers the following capabilities:
 - [x] Reviews
 - [ ] Auto-Translation
 - [ ] App detailed information
 - [ ] Store-wide search

## Installation
#### Maven
```xml
<dependency>
    <groupId>dev.fabiou.appvox</groupId>
    <artifactId>appvox-core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### Gradle
```groovy
compile "dev.fabiou.appvox:appvox-core:1.0.0-SNAPSHOT"
```

Only `appvox-core` package is mandatory to start using AppVox

| Package | Description |
|----------|---------|
| [`appvox-core`](./appvox-core) | Core package containing Google Play and App Store scrapers |
| [`appvox-examples`](./appvox-examples) | AppVox usage examples |

## Usage
#### Politeness
AppVox is polite by default. The default delay between requests is 500 milliseconds.
An `AppVoxException` will be thrown if user-defined request delay is inferior to the default one.

#### Through a proxy
Network requests can be made through a proxy.

All network calls made by AppVox can be made via a proxy by passing a `RequestConfiguration` object to `GooglePlay` or `AppStore` constructor
``` Kotlin
val config = RequestConfiguration(
    requestDelay = 3000L,
    proxy = ProxyConfiguration(
        host = "127.0.0.1",
        port = 8080
    )
)

val googlePlay = GooglePlay(config)
val appStore = AppStore(config)
// ...
```

#### Reviews
##### Google Play
Use default parameters to extract the 100 most recent English Twitter Android app reviews
```kotlin
import dev.fabiou.appvox.core.GooglePlay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GooglePlay().reviews("com.twitter.android")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
```

##### App Store
App Store scraper is a high-level scraper implementation requesting reviews from 2 different sources:
- Scraping apps.apple.com to return the whole review history of an app sorted by most relevant.
Moreover, the scrapped endpoint return no more than 10 reviews by request (fixed size by Apple).
This approach is limited if your goal is to stay up-to-date on the latest user reviews and do not include user app version.
That's why the tool offer a second way of getting the most recent App Store reviews
- Getting most recent reviews from Itunes RSS XML Feed. The JSON version of the Feed contains no review timestamp.
The RSS Feed returns the 500 most recent reviews at most and include more metadata such as app version and like count

Using both implementation is totally transparent for the user, just specify sortType to MOST_RECENT or MOST_RELEVANT to switch between the two methods
 
 Use default parameters to extract the 100 most recent English Twitter AppStore app reviews
``` Kotlin
import dev.fabiou.appvox.core.AppStore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    AppStore().reviews("333903271")
        .take(100)
        .collect { review ->
            println(review.toString())
        }
}
```

### Thread Safety
AppVox uses Kotlin Flows

### Dependencies
The library only depends on Jackson Kotlin module for JSON deserialization. Java SE 7 UrlConnection superclass handle http calls and JAXB deserializes iTunes XML Feed.

### Roadmap
The Roadmap and tasks in-progress of the project can be found in the upper "Projects" GitHub section