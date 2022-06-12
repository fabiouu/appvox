apply {
    plugin("org.jetbrains.kotlin.jvm")
    plugin("maven-publish")
}

dependencies {
    api(project(":appvox-core"))
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
}
