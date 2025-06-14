plugins {
    kotlin("jvm") version "2.1.21"
}
dependencies {
    api(project(":appvox-core"))
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}
