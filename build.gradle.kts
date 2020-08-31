plugins {
    `maven-publish`
    idea
    jacoco
    kotlin("jvm") version "1.3.72"
    id("com.github.fhermansson.assertj-generator") version "1.1.2"
    id("org.jmailen.kotlinter") version "2.3.2"
    id("pl.allegro.tech.build.axion-release") version "1.10.2"
    id("com.github.ben-manes.versions") version "0.28.0"
    id("com.jfrog.bintray") version "1.8.4"
}

apply {
    from("initial-version.gradle")
    from("checks.gradle")
    from("bintray.gradle")
    from("publishing.gradle")
}

group = "se.svt.oss"
version = scmVersion.version

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
    classOrPackageNames = arrayOf("se.svt.oss.mediaanalyzer")
    entryPointPackage = "se.svt.oss.mediaanalyzer"
}

repositories {
    jcenter()
    mavenCentral()
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.2")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
    testImplementation("io.mockk:mockk:1.10.0")
    testImplementation("org.assertj:assertj-core:3.16.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.4.1"
}