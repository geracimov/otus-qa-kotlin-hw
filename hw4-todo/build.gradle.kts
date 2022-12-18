plugins {
    kotlin("jvm") version "1.7.10"
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.22")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("net.serenity-bdd:serenity-core:3.5.0")
    testImplementation("net.serenity-bdd:serenity-junit:3.5.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}