import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.ashampoo"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.slack.api:slack-api-client:1.27.3")
    implementation("com.slack.api:slack-api-model-kotlin-extension:1.27.3")
    implementation("com.slack.api:slack-api-client-kotlin-extension:1.27.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
