apply {
    plugin("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(project(":appvox-appstore"))
    implementation(project(":appvox-googleplay"))
    implementation("com.opencsv:opencsv:5.4")
}
