plugins {
    kotlin("jvm") version "1.8.20"
}
dependencies {
    implementation(project(":appvox-appstore"))
    implementation(project(":appvox-googleplay"))
    implementation("com.opencsv:opencsv:5.6")
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(11)
}