
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.1.21" apply true
    `java-library`
    `maven-publish`
    signing
}

group = "com.github.fabiouu.appvox"
version = "0.0.1-SNAPSHOT"

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
//        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.apache.commons:commons-text:1.13.1")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
        testImplementation("io.kotest:kotest-assertions-core-jvm:4.4.3")
        testImplementation("com.github.tomakehurst:wiremock:2.27.2")
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_23)
            freeCompilerArgs.add("-Xjsr305=strict")
        }
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }

    tasks.test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "appvox-googleplay"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("AppVox")
                description.set("Capture the voice of your App users")
                url.set("https://github.com/fabiouu/appvox")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("https://github.com/fabiouu")
                        name.set("fabiouu")
                        email.set("fab.renoux@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:git@github.com:fabiouu/appvox.git")
                    developerConnection.set("scm:git:git@github.com:fabiouu/appvox.git")
                    url.set("https://github.com/fabiouu/appvox/tree/master")
                }
            }
            repositories {
                maven {
                    val releasesRepoUrl = uri(layout.buildDirectory.dir("repos/releases"))
                    val snapshotsRepoUrl = uri(layout.buildDirectory.dir("repos/snapshots"))
                    url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}
