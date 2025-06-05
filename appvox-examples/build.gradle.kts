plugins {
    kotlin("jvm") version "2.1.21"
}
dependencies {
    implementation(project(":appvox-appstore"))
    implementation(project(":appvox-googleplay"))
    implementation("com.opencsv:opencsv:5.6")
    implementation(kotlin("stdlib"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(23)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "23"
    targetCompatibility = "23"
}
