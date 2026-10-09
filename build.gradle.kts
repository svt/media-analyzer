import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    idea
    jacoco
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.9.25"
    id("com.github.fhermansson.assertj-generator") version "2.0.1"
    id("org.jmailen.kotlinter") version "3.16.0"
    id("pl.allegro.tech.build.axion-release") version "1.21.4"
    id("io.github.ben-manes.versions") version "0.64.0"
    id("org.jreleaser") version "1.26.0"
}

scmVersion {
    releaseOnlyOnReleaseBranches = true
    ignoreUncommittedChanges = false
    tag {
        prefix = "release"
        versionSeparator = "-"
    }
}

group = "se.svt.oss"
project.version = scmVersion.version

// axion-release 1.21.4 pulls in JGit 7.x, which removed GpgObjectSigner
// (replaced by the Signer API); jreleaser still compiles against it and
// dies with NoClassDefFoundError when Gradle 9's plugin classpath
// resolution lets axion's JGit win. Force a version both plugins work
// with. Revisit when jreleaser supports JGit 7.
buildscript {
    configurations.classpath {
        resolutionStrategy {
            force("org.eclipse.jgit:org.eclipse.jgit:6.10.0.202406032230-r")
        }
    }
}
project.description = "A media analyzer lib that utilizes ffprobe and mediainfo"

apply {
    from("checks.gradle")
    from("release.gradle")
}

tasks.test {
    useJUnitPlatform {
        if (providers.gradleProperty("skipIntegrationTests").isPresent) {
            excludeTags("integrationTest")
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

assertjGenerator {
    classOrPackageNames = listOf("se.svt.oss.mediaanalyzer", "org.apache.commons.math3.fraction")
    entryPointPackage = "se.svt.oss.mediaanalyzer"
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name = project.name
                description = project.description
                url = "https://github.com/svt/media-analyzer"
                inceptionYear = "2020"
                developers {
                    developer {
                        name = "Team Videocore"
                        email = "videcore@teams.svt.se"
                        organization = "SVT"
                        organizationUrl = "https://opensource.svt.se"
                    }
                }
                licenses {
                    license {
                        name = "Apache-2.0"
                        url = "https://spdx.org/licenses/Apache-2.0.html"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/svt/media-analyzer.git"
                    developerConnection = "scm:git:ssh://github.com/svt/media-analyzer.git"
                    url = "https://github.com/svt/media-analyzer"
                }
            }
        }
        repositories {
            maven {
                setUrl(layout.buildDirectory.dir("staging-deploy"))
            }
        }
    }
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.2")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    api("org.apache.commons:commons-math3:3.6.1")
    testImplementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    testImplementation("io.mockk:mockk:1.13.17")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation(platform("org.junit:junit-bom:5.14.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.6.4")
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        freeCompilerArgs.add("-Xjdk-release=17")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "9.8.0"
}
