import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    idea
    jacoco
    kotlin("jvm") version "1.9.25"
    id("com.github.fhermansson.assertj-generator") version "1.1.5"
    id("org.jmailen.kotlinter") version "3.10.0"
    id("pl.allegro.tech.build.axion-release") version "1.18.18"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("se.svt.oss.gradle-yapp-publisher") version "0.1.18"
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

apply {
    from("checks.gradle")
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
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
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
