package com.appvox.appvox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AppVoxApplication

fun main(args: Array<String>) {
	runApplication<AppVoxApplication>(*args)
}
