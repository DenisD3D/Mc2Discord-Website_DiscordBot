import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val kordexVersion: String by project
val exposedVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.5.10"
}

group = "ml.denisd3d.m2d"
version = "0.0.1"

application {
    mainClass.set("ml.denisd3d.m2d.ApplicationKt")
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(10, "minutes")
    resolutionStrategy.cacheDynamicVersionsFor(10, "minutes")
}

repositories {
    mavenCentral()

    maven {
        name = "Kotlin Discord"
        url = uri("https://maven.kotlindiscord.com/repository/maven-public/")
    }

    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-freemarker:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    //exposed to connect sql
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")

    // postgres SQL
    implementation("org.postgresql:postgresql:42.2.12")

    // Hikari configuration
    implementation("com.zaxxer:HikariCP:3.4.2")

    implementation("com.kotlindiscord.kord.extensions:kord-extensions:$kordexVersion")
    api("com.github.TheRandomLabs:CurseAPI:master-SNAPSHOT")
    api("com.github.TheRandomLabs:CurseAPI-Minecraft:master-SNAPSHOT")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

java {
    // Current LTS version of Java
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.register<Copy>("copyToLib") {
    from(configurations.runtimeClasspath)
    into("$buildDir/libs")
}

tasks.build {
    dependsOn(tasks["copyToLib"])
}