<h1 align="center">
  App Vox
</h1>

<p align="center">
    <a href="https://travis-ci.com/fabiouu/AppVox">
        <img src="https://travis-ci.com/fabiouu/AppVox.svg?branch=master" alt="Build Status" />
    </a>
    <a href="https://codeclimate.com/github/fabiouu/AppVox/maintainability">
        <img src="https://api.codeclimate.com/v1/badges/6f0c3287d031b4f431ea/maintainability" alt="Maintainability" />
    </a>
    <a href="https://codecov.io/gh/fabiouu/AppVox">
        <img src="https://codecov.io/gh/fabiouu/AppVox/branch/master/graph/badge.svg?token=AVB2DO0H4J" alt="Coverage" />
    </a>
</p>

<h3 align="center">
  Capture the voice of your App users from Google Play / App Store
</h3>

## Overview

## Getting Started
Maven

Gradle


## Features
### Reviews
Network requests can be made through a proxy (optional).
AppVox is polite by default, request delay cannot be less than 500 ms

#### Google Play

#### App Store
App Store scraper is a high-level scraper implementation requesting reviews from 2 different sources:
- Scraping apps.apple.com to return the whole review history of an app sorted by most relevant.
Moreover, the scrapped endpoint return no more than 10 reviews by request (fixed size by Apple).
This approach is limited if your goal is to stay up-to-date on the latest user reviews and do not include user app version.
That's why the tool offer a second way of getting the most recent App Store reviews
- Getting most recent reviews from Itunes RSS XML Feed. The JSON version of the Feed contains no review timestamp.
The RSS Feed returns the 500 most recent reviews at most and include more metadata such as app version and like count

Using both implementation is totally transparent for the user, just specify sortType to MOST_RECENT or MOST_RELEVANT to switch between the two methods
 

### Build


### Thread Safety


### Dependencies
The library only depends on Jackson Kotlin module for JSON deserialization. Java SE 7 UrlConnection superclass handle http calls and JAXB deserializes iTunes XML Feed.

### Roadmap
The Roadmap and tasks in-progress of the project can be found in the upper "Projects" GitHub section