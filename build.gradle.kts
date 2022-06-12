plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.0"
    `java-library`
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
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
        }
    }
}

allprojects {
    group = "io.appvox"
    version = "0.0.1"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
        runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
        testImplementation("io.kotest:kotest-assertions-core-jvm:4.4.3")
        testImplementation("com.github.tomakehurst:wiremock:2.27.2")
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    tasks.test {
        useJUnitPlatform()
    }
}
