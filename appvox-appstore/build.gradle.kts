plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    kotlin("jvm") version "1.8.20"
}

val implementation by configurations

dependencies {
    api(project(":appvox-core"))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.3")
    api("com.sun.activation:javax.activation:1.2.0")
    api("javax.xml.bind:jaxb-api:2.3.0")
    api("com.sun.xml.bind:jaxb-core:2.3.0")
    api("com.sun.xml.bind:jaxb-impl:2.3.0")
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(11)
}