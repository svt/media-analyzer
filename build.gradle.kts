plugins {
    idea
    jacoco
    kotlin("jvm") version "1.6.21"
    id("com.github.fhermansson.assertj-generator") version "1.1.4"
    id("org.jmailen.kotlinter") version "3.10.0"
    id("pl.allegro.tech.build.axion-release") version "1.13.9"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.16"
    id("se.svt.oss.gradle-yapp-publisher") version "0.1.18"
}

scmVersion.tag.prefix = "release"
scmVersion.tag.versionSeparator = "-"

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

kotlinter {
    disabledRules = arrayOf("import-ordering")
}

assertjGenerator {
    classOrPackageNames = arrayOf("se.svt.oss.mediaanalyzer", "org.apache.commons.math3.fraction")
    entryPointPackage = "se.svt.oss.mediaanalyzer"
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("io.github.microutils:kotlin-logging:2.1.23")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    api("org.apache.commons:commons-math3:3.6.1")
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.11")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}
tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "7.4.2"
}
