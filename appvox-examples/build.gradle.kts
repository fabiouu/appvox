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
    jvmToolchain(21)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}
