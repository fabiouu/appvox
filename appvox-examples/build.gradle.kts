plugins {
    id("com.appvox.java-conventions")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.72")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    implementation(project(":appvox-core"))
    implementation("com.opencsv:opencsv:5.2")
}

description = "appvox-examples"
