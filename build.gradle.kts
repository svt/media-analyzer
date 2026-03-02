import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    idea
    jacoco
    `java-library`
    `maven-publish`
    kotlin("jvm") version "1.9.25"
    id("com.github.fhermansson.assertj-generator") version "1.1.5"
    id("org.jmailen.kotlinter") version "3.10.0"
    id("pl.allegro.tech.build.axion-release") version "1.18.18"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.jreleaser") version "1.20.0"
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
project.description = "A media analyzer lib that utilizes ffprobe and mediainfo"

apply {
    from("checks.gradle")
    from("release.gradle")
}

tasks.test {
    useJUnitPlatform {
        if (project.hasProperty("runIntegrationTest")) {
            excludeTags("integrationTest")
        }
    }
}

tasks.lintKotlinTest {
    source = (source - fileTree("src/test/generated-java")).asFileTree
}
tasks.formatKotlinTest {
    source = (source - fileTree("src/test/generated-java")).asFileTree
}

kotlinter {
    disabledRules = arrayOf("import-ordering")
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
    classOrPackageNames = arrayOf("se.svt.oss.mediaanalyzer", "org.apache.commons.math3.fraction")
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
    api("org.apache.commons:commons-math3:3.6.1")
    testImplementation("javax.annotation:javax.annotation-api:1.3.2")
    testImplementation("io.mockk:mockk:1.13.17")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation(platform("org.junit:junit-bom:5.12.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.5.17")
}

kotlin {
    jvmToolchain(17)
}
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "8.14.3"
}
