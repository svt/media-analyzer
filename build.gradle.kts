plugins {
    id("maven-publish")
    idea
    jacoco
    kotlin("jvm") version "1.4.31"
    id("com.github.fhermansson.assertj-generator") version "1.1.2"
    id("org.jmailen.kotlinter") version "3.4.5"
    id("pl.allegro.tech.build.axion-release") version "1.13.3"
    id("se.ascp.gradle.gradle-versions-filter") version "0.1.10"
    id("se.svt.oss.gradle-yapp-publisher-plugin") version "0.1.15"
}


group = "se.svt.oss"
project.version = scmVersion.version

apply {
    from("initial-version.gradle")
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

repositories {
    mavenCentral()
}


dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
    implementation("io.github.microutils:kotlin-logging:2.0.10")
    api("org.apache.commons:commons-math3:3.6.1")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.5")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.9"
}
